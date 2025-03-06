-- Active: 1724543067802@@mysql@3306@devdb
CREATE DATABASE IF NOT EXISTS ecommerce_products
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_products.* TO 'devuser'@'%';

CREATE DATABASE IF NOT EXISTS ecommerce_customers
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_customers.* TO 'devuser'@'%';

CREATE DATABASE IF NOT EXISTS ecommerce_orders
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_orders.* TO 'devuser'@'%';

CREATE DATABASE IF NOT EXISTS ecommerce_users_auth
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_users_auth.* TO 'devuser'@'%';

CREATE DATABASE IF NOT EXISTS ecommerce_filestorage
    DEFAULT CHARACTER SET = 'utf8mb4';

GRANT ALL PRIVILEGES ON ecommerce_filestorage.* TO 'devuser'@'%';

FLUSH PRIVILEGES;