-- Queries Hotel API--

-- 1. Total de clientes
SELECT COUNT(*) AS total_clientes
FROM customers;

-- 2. Total de quartos
SELECT COUNT(*) AS total_quartos
FROM rooms;

-- 3. Reservas em aberto
SELECT COUNT(*) AS reservas_em_aberto
FROM reservations
WHERE status = 'OPEN';

-- 4. Quartos vagos
SELECT COUNT(*) AS quartos_vagos
FROM rooms r
WHERE r.id NOT IN (
    SELECT room_id
    FROM reservations
    WHERE status = 'IN_USE'
);

-- 5. Quartos ocupados
SELECT COUNT(DISTINCT room_id) AS quartos_ocupados
FROM reservations
WHERE status = 'IN_USE';

-- 6. Reservas futuras
SELECT COUNT(*) AS reservas_futuras
FROM reservations
WHERE check_in > CURDATE()
  AND status IN ('OPEN', 'IN_USE');

-- 7. Quarto mais caro
SELECT id, room_number, type, price
FROM rooms
ORDER BY price DESC
    LIMIT 1;

-- 8. Quarto com mais cancelamentos
SELECT r.id, r.room_number, COUNT(*) AS total_cancelamentos
FROM reservations res
         JOIN rooms r ON r.id = res.room_id
WHERE res.status = 'CANCELLED'
GROUP BY r.id, r.room_number
ORDER BY total_cancelamentos DESC
    LIMIT 1;

-- 9. Clientes por quarto
SELECT r.id, r.room_number,
       COUNT(DISTINCT res.customer_id) AS total_clientes
FROM rooms r
         LEFT JOIN reservations res ON res.room_id = r.id
GROUP BY r.id, r.room_number
ORDER BY r.id;

-- 10. 3 quartos com mais ocupações
SELECT r.id, r.room_number, COUNT(*) AS total_ocupacoes
FROM reservations res
         JOIN rooms r ON r.id = res.room_id
GROUP BY r.id, r.room_number
ORDER BY total_ocupacoes DESC
    LIMIT 3;

-- 11. Clientes com mais reservas
SELECT c.id, c.name, c.email,
       COUNT(res.id) AS total_reservas
FROM customers c
         JOIN reservations res ON res.customer_id = c.id
GROUP BY c.id, c.name, c.email
ORDER BY total_reservas DESC
    LIMIT 10;

-- 12. Média do valor das reservas
SELECT AVG(r.price) AS receita_media
FROM reservations res
         JOIN rooms r ON r.id = res.room_id
WHERE res.status IN ('FINISHED', 'IN_USE');