package com.arrivo.security

import com.arrivo.employee.EmployeeRepository
import com.arrivo.utilities.Settings.Companion.INVALID_TOKEN_MESSAGE
import com.arrivo.utilities.Settings.Companion.USER_NOT_FOUND_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class FirebaseTokenFilter(private val employeeRepository: EmployeeRepository) : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val token = getBearerToken(httpRequest)

        if (token != null) {
            try {
                val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
                val firebaseUid = firebaseToken.uid

                val employee =
                    employeeRepository.findByFirebaseUid(firebaseUid) ?: throw Exception(USER_NOT_FOUND_MESSAGE)

                val authorities = mutableListOf<GrantedAuthority>()
                authorities.add(SimpleGrantedAuthority("ROLE_${employee.role}"))

                val authentication = PreAuthenticatedAuthenticationToken(firebaseUid, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: Exception) {
                println(e.message)
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_TOKEN_MESSAGE)
                return
            }
        }

        chain.doFilter(request, response)
    }

    private fun getBearerToken(request: HttpServletRequest): String? {
        var bearer = request.getHeader("Authorization")
        return if (bearer != null && bearer.startsWith("Bearer ")) {
            bearer.substring(7)
        } else null
    }
}


/*@Component
class FirebaseTokenFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse
        val token = getBearerToken(httpRequest)
//        println("TOKEN: " + token)

        if (token != null) {
            try {
                val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)

                val authentication = PreAuthenticatedAuthenticationToken(
                    firebaseToken, null, emptyList()
                )

                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: Exception) {
                println(e.message)
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_TOKEN_MESSAGE)
                return
            }
        }

        chain.doFilter(request, response)
    }

    private fun getBearerToken(request: HttpServletRequest): String? {
        var bearer = request.getHeader("Authorization")
        return if (bearer != null && bearer.startsWith("Bearer ")) {
            bearer = bearer.substring(7)
            bearer
        } else null
    }

}*/