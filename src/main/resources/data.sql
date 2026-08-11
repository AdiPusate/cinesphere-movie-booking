-- Cinesphere sample data.
-- Hibernate creates/updates the schema before this script runs.

INSERT IGNORE INTO movies
(movie_id, title, genre, duration, language, description, release_date, poster_url, banner_url, rating, status)
VALUES
(1, 'RRR', 'ACTION', 187, 'TELUGU',
 'A fictitious story about two legendary revolutionaries and their journey away from home.',
 '2022-03-24',
 'https://image.tmdb.org/t/p/w500/nEufeZlyAOLqO2brrs0yeF1lgHO.jpg',
 'https://image.tmdb.org/t/p/original/7ZO9yoEU2fAHKhmJWfAc2QIPWJg.jpg',
 8.8, 'NOW_SHOWING'),
(2, 'Dangal', 'DRAMA', 161, 'HINDI',
 'Former wrestler Mahavir Singh Phogat and his two wrestler daughters struggle towards glory at the Commonwealth Games.',
 '2016-12-23',
 'https://image.tmdb.org/t/p/w500/yJEI5TncT9nEqhS4H7613z6h2lX.jpg',
 'https://image.tmdb.org/t/p/original/bblvEEivEhm3YI2fP6k31o35kQO.jpg',
 8.4, 'NOW_SHOWING');

INSERT IGNORE INTO theatres
(theatre_id, name, city, address)
VALUES
(1, 'Cinesphere IMAX', 'Pune', 'Cinesphere Avenue, Pune');

INSERT IGNORE INTO shows
(show_id, movie_id, theatre_id, show_time, base_price)
VALUES
(1, 1, 1, '2026-08-15 21:30:00', 250.00),
(2, 2, 1, '2026-08-15 18:30:00', 300.00);

INSERT IGNORE INTO coupons
(coupon_id, code, discount_percentage, expiry_date, is_active)
VALUES
(1, 'CINESPHERE10', 10, '2026-12-31', TRUE);

-- Sample 5 x 10 seating for the seeded shows.
INSERT IGNORE INTO show_seats (show_id, seat_number, status)
VALUES
(1,'A1','AVAILABLE'),(1,'A2','AVAILABLE'),(1,'A3','AVAILABLE'),(1,'A4','AVAILABLE'),(1,'A5','AVAILABLE'),(1,'A6','AVAILABLE'),(1,'A7','AVAILABLE'),(1,'A8','AVAILABLE'),(1,'A9','AVAILABLE'),(1,'A10','AVAILABLE'),
(1,'B1','AVAILABLE'),(1,'B2','AVAILABLE'),(1,'B3','AVAILABLE'),(1,'B4','AVAILABLE'),(1,'B5','AVAILABLE'),(1,'B6','AVAILABLE'),(1,'B7','AVAILABLE'),(1,'B8','AVAILABLE'),(1,'B9','AVAILABLE'),(1,'B10','AVAILABLE'),
(1,'C1','AVAILABLE'),(1,'C2','AVAILABLE'),(1,'C3','AVAILABLE'),(1,'C4','AVAILABLE'),(1,'C5','AVAILABLE'),(1,'C6','AVAILABLE'),(1,'C7','AVAILABLE'),(1,'C8','AVAILABLE'),(1,'C9','AVAILABLE'),(1,'C10','AVAILABLE'),
(1,'D1','AVAILABLE'),(1,'D2','AVAILABLE'),(1,'D3','AVAILABLE'),(1,'D4','AVAILABLE'),(1,'D5','AVAILABLE'),(1,'D6','AVAILABLE'),(1,'D7','AVAILABLE'),(1,'D8','AVAILABLE'),(1,'D9','AVAILABLE'),(1,'D10','AVAILABLE'),
(1,'E1','AVAILABLE'),(1,'E2','AVAILABLE'),(1,'E3','AVAILABLE'),(1,'E4','AVAILABLE'),(1,'E5','AVAILABLE'),(1,'E6','AVAILABLE'),(1,'E7','AVAILABLE'),(1,'E8','AVAILABLE'),(1,'E9','AVAILABLE'),(1,'E10','AVAILABLE'),
(2,'A1','AVAILABLE'),(2,'A2','AVAILABLE'),(2,'A3','AVAILABLE'),(2,'A4','AVAILABLE'),(2,'A5','AVAILABLE'),(2,'A6','AVAILABLE'),(2,'A7','AVAILABLE'),(2,'A8','AVAILABLE'),(2,'A9','AVAILABLE'),(2,'A10','AVAILABLE'),
(2,'B1','AVAILABLE'),(2,'B2','AVAILABLE'),(2,'B3','AVAILABLE'),(2,'B4','AVAILABLE'),(2,'B5','AVAILABLE'),(2,'B6','AVAILABLE'),(2,'B7','AVAILABLE'),(2,'B8','AVAILABLE'),(2,'B9','AVAILABLE'),(2,'B10','AVAILABLE'),
(2,'C1','AVAILABLE'),(2,'C2','AVAILABLE'),(2,'C3','AVAILABLE'),(2,'C4','AVAILABLE'),(2,'C5','AVAILABLE'),(2,'C6','AVAILABLE'),(2,'C7','AVAILABLE'),(2,'C8','AVAILABLE'),(2,'C9','AVAILABLE'),(2,'C10','AVAILABLE'),
(2,'D1','AVAILABLE'),(2,'D2','AVAILABLE'),(2,'D3','AVAILABLE'),(2,'D4','AVAILABLE'),(2,'D5','AVAILABLE'),(2,'D6','AVAILABLE'),(2,'D7','AVAILABLE'),(2,'D8','AVAILABLE'),(2,'D9','AVAILABLE'),(2,'D10','AVAILABLE'),
(2,'E1','AVAILABLE'),(2,'E2','AVAILABLE'),(2,'E3','AVAILABLE'),(2,'E4','AVAILABLE'),(2,'E5','AVAILABLE'),(2,'E6','AVAILABLE'),(2,'E7','AVAILABLE'),(2,'E8','AVAILABLE'),(2,'E9','AVAILABLE'),(2,'E10','AVAILABLE');
