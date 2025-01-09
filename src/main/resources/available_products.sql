INSERT INTO available_product (name)
VALUES
    ('Apple juice'),
    ('Banana pack 5kg'),
    ('Milk cartoons box 6 pcs'),
    ('Bread'),
    ('Orange juice 1L'),
    ('Rice bag 1kg'),
    ('Pasta pack 500g'),
    ('Chicken breast 1kg'),
    ('Eggs 12 pcs'),
    ('Butter 250g'),
    ('Cheddar cheese 200g'),
    ('Tomato sauce jar 500ml'),
    ('Coffee beans 1kg'),
    ('Green tea 100g'),
    ('Cereal box 750g'),
    ('Honey jar 500g'),
    ('Strawberry jam 300g'),
    ('Peanut butter 500g'),
    ('Mineral water 1.5L'),
    ('Sparkling water 1.5L'),
    ('Coca-Cola 2L bottle'),
    ('Frozen pizza'),
    ('Ice cream tub 1L'),
    ('Chocolate bar'),
    ('Potato chips bag 150g'),
    ('Carrots 1kg'),
    ('Potatoes 5kg bag'),
    ('Onions 2kg'),
    ('Tomatoes 1kg'),
    ('Apples 1kg'),
    ('Grapes 500g'),
    ('Cucumber'),
    ('Lettuce'),
    ('Spinach 500g bag'),
    ('Yogurt 1kg'),
    ('Whole grain bread'),
    ('Oats 1kg'),
    ('Almonds 200g'),
    ('Cashews 200g'),
    ('Cooking oil 1L'),
    ('Olive oil 500ml'),
    ('Salt 1kg'),
    ('Pepper shaker 50g'),
    ('Sugar 1kg'),
    ('Flour 2kg'),
    ('Yeast 100g'),
    ('Pineapple can 400g'),
    ('Mangoes 1kg'),
    ('Strawberries 500g'),
    ('Raspberries 300g'),
    ('Watermelon 1kg'),
    ('Papaya 1 piece'),
    ('Chili peppers 200g'),
    ('Garlic bulbs'),
    ('Cabbage 1kg'),
    ('Sweet potatoes 2kg'),
    ('Peas frozen 500g'),
    ('Zucchini 1kg'),
    ('Cherries 500g'),
    ('Blueberries 200g'),
    ('Kiwi fruits 4 pcs'),
    ('Banana pack 20kg')
    ON CONFLICT (name) DO NOTHING;
