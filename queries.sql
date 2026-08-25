-- Queries Hotel API

-- 1. Quantos clientes temos na base?
SELECT COUNT(*) AS total_clientes
FROM customers;


-- 2. Quantos quartos temos cadastrados?
SELECT COUNT(*) AS total_quartos
FROM rooms;


-- 3. Quantas reservas em aberto o hotel possui no momento?
SELECT COUNT(*) AS reservas_em_aberto
FROM reservations
WHERE status = 'OPEN';


-- 4. Quantos quartos temos vagos no momento?
SELECT COUNT(*) AS quartos_vagos
FROM rooms r
WHERE r.id NOT IN (
    SELECT room_id
    FROM reservations
    WHERE status = 'IN_USE'
);


-- 5. Quantos quartos temos ocupados no momento?
SELECT COUNT(DISTINCT room_id) AS quartos_ocupados
FROM reservations
WHERE status = 'IN_USE';


-- 6. Quantas reservas futuras o hotel possui?
SELECT COUNT(*) AS reservas_futuras
FROM reservations
WHERE check_in > CURDATE()
  AND status IN ('OPEN', 'IN_USE');


-- 7. Qual o quarto mais caro do hotel?
SELECT id, room_number, type, price
FROM rooms
ORDER BY price DESC
    LIMIT 1;


-- 8. Qual o quarto com maior histórico de cancelamentos?
SELECT r.id,
       r.room_number,
       COUNT(*) AS total_cancelamentos
FROM reservations res
         JOIN rooms r ON r.id = res.room_id
WHERE res.status = 'CANCELLED'
GROUP BY r.id, r.room_number
ORDER BY total_cancelamentos DESC
    LIMIT 1;


-- 9. Liste todos os quartos e a quantidade de clientes
SELECT r.id,
       r.room_number,
       COUNT(DISTINCT res.customer_id) AS total_clientes
FROM rooms r
         LEFT JOIN reservations res ON res.room_id = r.id
GROUP BY r.id, r.room_number
ORDER BY r.id;


-- 10. Quais são os 3 quartos que possuem
SELECT r.id,
       r.room_number,
       COUNT(*) AS total_ocupacoes
FROM reservations res
         JOIN rooms r ON r.id = res.room_id
GROUP BY r.id, r.room_number
ORDER BY total_ocupacoes DESC
    LIMIT 3;


-- 11. Os 10 clientes com maior histórico de reservas
SELECT c.id,
       c.name,
       c.email,
       COUNT(res.id) AS total_reservas
FROM customers c
         JOIN reservations res ON res.customer_id = c.id
GROUP BY c.id, c.name, c.email
ORDER BY total_reservas DESC
    LIMIT 10;


-- 12. Receita média gerada,
SELECT AVG(r.price) AS receita_media
FROM reservations res
         JOIN rooms r ON r.id = res.room_id
WHERE res.status IN ('FINISHED', 'IN_USE');
