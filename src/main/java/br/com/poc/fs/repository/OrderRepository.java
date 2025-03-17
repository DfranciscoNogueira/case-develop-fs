package br.com.poc.fs.repository;

import br.com.poc.fs.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserUserName(String userName);

    Optional<Order> findByIdAndUserUserName(String id, String userName);

    @Query(value = """
            SELECT u.id, u.user_name, u.email, SUM(o.total) AS totalPurchases FROM tb_order o
            JOIN tb_user u ON o.user_id = u.id
            GROUP BY u.id, u.user_name, u.email
            ORDER BY totalPurchases DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Object[]> findTopFiveUsersByTotalPurchases();

    @Query(value = """
            SELECT SUM(o.total) AS totalInvoiced FROM tb_order o
            WHERE MONTH(o.updated_at) = MONTH(CURRENT_DATE)
            AND YEAR(o.updated_at) = YEAR(CURRENT_DATE)
            AND o.status = 'COMPLETED'
            """, nativeQuery = true)
    Double calculateMonthlyBilling();

    @Query(value = """
            SELECT u.id, u.user_name, u.email,
            SUM(o.total) AS totalPurchases, COUNT(o.id) AS numberOfOrders,
            (SUM(o.total) / COUNT(o.id)) AS ticketAverage
            FROM tb_order o
            JOIN tb_user u ON o.user_id = u.id
            WHERE o.status = 'COMPLETED'
            GROUP BY u.id, u.nome, u.email
            ORDER BY ticketAverage DESC
            """, nativeQuery = true)
    List<Object[]> calculateAverageTicketPerUser();

}
