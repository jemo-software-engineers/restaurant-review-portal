-- Create sequence for user_table
CREATE SEQUENCE user_table_id_seq START 1;

-- Create sequence for review
CREATE SEQUENCE review_id_seq START 1;

-- Create sequence for restaurant
CREATE SEQUENCE restaurant_id_seq START 1;

-- Create sequence for rating
CREATE SEQUENCE rating_id_seq START 1;

-- Create sequence for comment
CREATE SEQUENCE comment_id_seq START 1;

-- Create sequence for menu
CREATE SEQUENCE menu_id_seq START 1;

-- Create sequence for menuitem
CREATE SEQUENCE menuitem_id_seq START 1;

-- Create user_table
CREATE TABLE user_table (
                            id BIGINT PRIMARY KEY DEFAULT nextval('user_table_id_seq'),
                            username VARCHAR(255) NOT NULL UNIQUE,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create restaurant table
CREATE TABLE restaurant (
                            id BIGINT PRIMARY KEY DEFAULT nextval('restaurant_id_seq'),
                            name VARCHAR(255) NOT NULL UNIQUE,
                            address VARCHAR(255) NOT NULL,
                            phone VARCHAR(50) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            website VARCHAR(255) NOT NULL,
                            city VARCHAR(100) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            cuisine VARCHAR(50) NOT NULL,
                            average_rating DOUBLE PRECISION
);

-- Create review table
CREATE TABLE review (
                        id BIGINT PRIMARY KEY DEFAULT nextval('review_id_seq'),
                        user_id BIGINT NOT NULL,
                        restaurant_id BIGINT NOT NULL,
                        review_text TEXT,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES user_table(id) ON DELETE CASCADE,
                        FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);

-- Create rating table
CREATE TABLE rating (
                        id BIGINT PRIMARY KEY DEFAULT nextval('rating_id_seq'),
                        review_id BIGINT NOT NULL,
                        food_quality DOUBLE PRECISION NOT NULL,
                        customer_service DOUBLE PRECISION NOT NULL,
                        cleanliness_and_hygiene DOUBLE PRECISION NOT NULL,
                        ambiance DOUBLE PRECISION NOT NULL,
                        value_for_money DOUBLE PRECISION NOT NULL,
                        overall_rating DOUBLE PRECISION NOT NULL,
                        FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
);

-- Create comment table
CREATE TABLE comment (
                         id BIGINT PRIMARY KEY DEFAULT nextval('comment_id_seq'),
                         comment_text TEXT NOT NULL,
                         review_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE,
                         FOREIGN KEY (user_id) REFERENCES user_table(id) ON DELETE CASCADE
);

-- Create menu table
CREATE TABLE menu (
                      id BIGINT PRIMARY KEY DEFAULT nextval('menu_id_seq'),
                      name VARCHAR(255) NOT NULL,
                      description TEXT NOT NULL,
                      restaurant_id BIGINT NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      FOREIGN KEY (restaurant_id) REFERENCES restaurant(id) ON DELETE CASCADE
);

-- Create menuitem table
CREATE TABLE menuitem (
                          id BIGINT PRIMARY KEY DEFAULT nextval('menuitem_id_seq'),
                          name VARCHAR(255) NOT NULL,
                          description TEXT NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          menu_id BIGINT NOT NULL,
                          availability VARCHAR(50) NOT NULL,
                          dietary_info TEXT NOT NULL,
                          average_rating DOUBLE PRECISION,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE
);
