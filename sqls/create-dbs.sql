CREATE DATABASE ecommerce_products
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_products.* TO 'devuser'@'%';

CREATE DATABASE ecommerce_customers
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_customers.* TO 'devuser'@'%';

CREATE DATABASE ecommerce_orders
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_orders.* TO 'devuser'@'%';

CREATE DATABASE ecommerce_users_auth
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_users_auth.* TO 'devuser'@'%';