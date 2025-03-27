package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.snapbizz.core.database.InvoiceInfo
import com.snapbizz.core.database.InvoiceWithItems
import com.snapbizz.core.database.entities.Invoice

@Dao
interface InvoiceDao : GenericDao<Invoice> {

    @Query("SELECT * FROM INVOICES WHERE _id = :id")
    suspend fun getInvoiceById(id: Long): Invoice?


    @Query("DELETE FROM INVOICES WHERE _id = :id")
    suspend fun deleteInvoiceById(id: Long)

    @Query("SELECT * FROM INVOICES")
    suspend fun getAllInvoices(): List<Invoice>

    @Query("DELETE FROM INVOICES")
    fun deleteAll()

    @Query("SELECT MAX(_id) FROM INVOICES")
    fun getLastInvoiceId(): Long?

    @Query(
        "SELECT COALESCE(SUM(NET_AMOUNT-TOTAL_DISCOUNT), 0) AS totalAmount FROM INVOICES i LEFT JOIN CUSTOMERS c ON i.CUSTOMER_PHONE = c.PHONE " + "WHERE (:query IS NULL OR _id LIKE '%' || :query || '%' " + "OR c.NAME LIKE '%' || :query || '%' " + "OR CUSTOMER_PHONE LIKE '%' || :query || '%') " + "AND i.CREATED_AT BETWEEN :startDate AND :endDate"
    )
    fun getBillsSummary(query: String?, startDate: Long, endDate: Long): Long?

    @Transaction
    @Query("SELECT * FROM INVOICES WHERE _id = :invoiceId")
    fun getInvoiceWithItems(invoiceId: Long): InvoiceWithItems

//    @Query(
//        "SELECT i.*, c.NAME From INVOICES i Left Join CUSTOMERS c on i.CUSTOMER_PHONE = c.PHONE " + "WHERE (:query IS NULL OR _id LIKE '%' || :query || '%' " + "OR c.NAME LIKE '%' || :query || '%' " + "OR CUSTOMER_PHONE LIKE '%' || :query || '%') " + "AND i.CREATED_AT BETWEEN :startDate AND :endDate " + " ORDER BY i.BILL_STARTED_AT DESC LIMIT :limit OFFSET :offset "
//    )
//    suspend fun getInvoice(
//        query: String?, offset: Int?, limit: Int?, startDate: Long, endDate: Long
//    ): List<InvoiceInfo>
//
//    @Query("SELECT * FROM INVOICES WHERE CUSTOMER_PHONE = :phone ORDER BY BILL_STARTED_AT DESC LIMIT :limit OFFSET :offset")
//    suspend fun getInvoiceByCustomerPhone(phone: Long, offset: Int, limit: Int): List<InvoiceInfo>
//
//    @Query(
//        """
//    SELECT
//        sum(CASE WHEN (PAYMENT_MODE = 'CASH' OR (PAYMENT_MODE = 'DIGITAL' AND TRANSACTION_TYPE = 'CASH')) THEN AMOUNT ELSE 0 END) AS cashSales,
//        sum(CASE WHEN (PAYMENT_MODE = 'DIGITAL' AND (TRANSACTION_TYPE = 'DEBIT_CARD' OR TRANSACTION_TYPE = 'CREDIT_CARD')) THEN (AMOUNT-MDR_VALUE) ELSE 0 END ) AS cardSales,
//        sum(CASE WHEN (PAYMENT_MODE = 'DIGITAL' AND TRANSACTION_TYPE = 'UPI') THEN AMOUNT ELSE 0 END) AS upiSales,
//        sum(CASE WHEN (PAYMENT_MODE = 'DIGITAL' AND (TRANSACTION_TYPE = 'DEBIT_CARD' OR TRANSACTION_TYPE = 'CREDIT_CARD')) THEN MDR_VALUE ELSE 0 END ) AS mdrValue,
//        sum(AMOUNT-MDR_VALUE) AS totalSales
//    FROM TRANSACTIONS
//    WHERE CREATED_AT BETWEEN :startDate AND :endDate
//      AND TRANSACTION_DESCRIPTION = 'SUCCESS'
//    """
//    )
//    suspend fun getPaymentSummaryData(startDate: Long, endDate: Long): PaymentTrxSummaryData
//
//    @Query("SELECT * FROM TRANSACTIONS WHERE (PAYMENT_MODE = 'DIGITAL' OR PAYMENT_MODE = 'CASH') " +
//            "AND (TRANSACTION_DESCRIPTION = 'SUCCESS' OR TRANSACTION_DESCRIPTION IS NULL) " +
//            "AND CREATED_AT BETWEEN :startDate AND :endDate " +
//            "ORDER BY CREATED_AT DESC LIMIT :limit OFFSET :offset ")
//    suspend fun getAllTransactions(startDate: Long, endDate: Long, offset: Int, limit: Int) : List<TransactionInfo>
//
//    @Query(
//        """
//    SELECT
//        COUNT(DISTINCT invoice_summary.invoice_id) AS totalInvoices,
//        SUM( invoice_summary.mdr_value) AS totalMdr,
//        ROUND(SUM(invoice_summary.total_sales)-SUM(invoice_summary.total_discount), 2) AS totalSales,
//        SUM(invoice_summary.total_quantity) AS totalQuantity,
//        (SUM(invoice_summary.total_sales) + SUM(invoice_summary.total_discount) - COALESCE(invoice_profit.profit, 0)) AS totalCost,
//        ROUND(SUM(COALESCE(invoice_profit.profit, 0)), 2) AS totalProfit
//    FROM (
//        SELECT
//            _id AS invoice_id,
//            MDR_VALUE AS mdr_value,
//            NET_AMOUNT AS total_sales,
//            TOTAL_DISCOUNT AS total_discount,
//            TOTAL_QUANTITY AS total_quantity
//        FROM invoices
//        WHERE CREATED_AT BETWEEN :startDate AND :endDate
//    ) AS invoice_summary
//    LEFT JOIN (
//        SELECT
//            items.INVOICE_ID,
//            SUM(
//                CASE
//                    WHEN items.UOM = 'PC' THEN
//                        (COALESCE(items.SALE_PRICE, 0) - COALESCE(items.PURCHASE_PRICE, 0)) * items.QUANTITY
//                    ELSE
//                        (COALESCE(items.SALE_PRICE, 0) - COALESCE(items.PURCHASE_PRICE, 0)) * items.QUANTITY / 1000
//                END
//            ) AS profit
//        FROM items
//        GROUP BY items.INVOICE_ID
//    ) AS invoice_profit
//    ON invoice_summary.invoice_id = invoice_profit.INVOICE_ID
//"""
//    )
//    suspend fun getReportSummary(startDate: Long, endDate: Long): ReportSummaryItem
//
//    @Query(
//        """
//        WITH FilteredInvoices AS (
//            SELECT
//                _id AS invoiceId,
//                NET_AMOUNT-TOTAL_DISCOUNT AS netAmount,
//                CREATED_AT
//            FROM invoices
//            WHERE CREATED_AT BETWEEN :monStart AND :sunEnd
//        ),
//        DailySales AS (
//            SELECT
//                netAmount,
//                CASE WHEN CREATED_AT BETWEEN :monStart AND :monEnd THEN netAmount ELSE 0 END AS monSale,
//                CASE WHEN CREATED_AT BETWEEN :tueStart AND :tueEnd THEN netAmount ELSE 0 END AS tueSale,
//                CASE WHEN CREATED_AT BETWEEN :wedStart AND :wedEnd THEN netAmount ELSE 0 END AS wedSale,
//                CASE WHEN CREATED_AT BETWEEN :thuStart AND :thuEnd THEN netAmount ELSE 0 END AS thuSale,
//                CASE WHEN CREATED_AT BETWEEN :friStart AND :friEnd THEN netAmount ELSE 0 END AS friSale,
//                CASE WHEN CREATED_AT BETWEEN :satStart AND :satEnd THEN netAmount ELSE 0 END AS satSale,
//                CASE WHEN CREATED_AT BETWEEN :sunStart AND :sunEnd THEN netAmount ELSE 0 END AS sunSale
//            FROM FilteredInvoices
//        )
//        SELECT
//            SUM(IFNULL(ROUND(CAST(netAmount AS FLOAT) / 100, 2), 0)) AS totalAmount,
//            SUM(IFNULL(ROUND(CAST(monSale AS FLOAT) / 100, 2), 0)) AS monSale,
//            SUM(IFNULL(ROUND(CAST(tueSale AS FLOAT) / 100, 2), 0)) AS tueSale,
//            SUM(IFNULL(ROUND(CAST(wedSale AS FLOAT) / 100, 2), 0)) AS wedSale,
//            SUM(IFNULL(ROUND(CAST(thuSale AS FLOAT) / 100, 2), 0)) AS thuSale,
//            SUM(IFNULL(ROUND(CAST(friSale AS FLOAT) / 100, 2), 0)) AS friSale,
//            SUM(IFNULL(ROUND(CAST(satSale AS FLOAT) / 100, 2), 0)) AS satSale,
//            SUM(IFNULL(ROUND(CAST(sunSale AS FLOAT) / 100, 2), 0)) AS sunSale
//        FROM DailySales
//    """
//    )
//    suspend fun getSaleChartSummary(
//        monStart: Long,
//        monEnd: Long,
//        tueStart: Long,
//        tueEnd: Long,
//        wedStart: Long,
//        wedEnd: Long,
//        thuStart: Long,
//        thuEnd: Long,
//        friStart: Long,
//        friEnd: Long,
//        satStart: Long,
//        satEnd: Long,
//        sunStart: Long,
//        sunEnd: Long
//    ): DailySalesSummary
//
//    @Query(
//        """
//        SELECT
//            invoices.bill_to_gstin AS invoicesBillToGstin,
//            IFNULL(customers.name, '') AS customerName,
//            transactions.TRANSACTION_TYPE AS paymentMode,
//            invoices._id AS id,
//            invoices.is_memo AS isMemo,
//            invoices.created_at AS createdAt,
//            invoices.net_amount AS invoiceValue,
//            invoices.round_off_amount AS roundOffAmount,
//            IFNULL(items.hsn_code, '') AS hsn,
//            IFNULL(items.name, '') AS productName,
//            items.quantity AS quantity,
//            items.farmer_share as farmerShare,
//            items.agro_charge as agroCharge,
//            items.concerned_ddo as ddo,
//            (
//                (CASE
//                    WHEN items.uom = 'PC'
//                    THEN items.sale_price * items.quantity
//                    ELSE items.sale_price * (CAST(items.quantity AS FLOAT) / 1000.0)
//                END)
//                - (
//    CASE
//        WHEN items.igst_amount IS NULL OR items.igst_amount = 0
//        THEN items.sgst_amount + items.cgst_amount
//        ELSE items.igst_amount
//    END
//    + items.cess_amount
//    + items.additional_cess_amount
//)
//            ) AS taxableValue,
//            items.sgst_rate AS itemsSgstRate,
//            CAST(items.sgst_amount AS TEXT) AS sgstAmt,
//            items.cgst_rate AS itemsCgstRate,
//            CAST(items.cgst_amount AS TEXT) AS cgstAmt,
//            items.igst_rate AS itemsIgstRate,
//            CAST(items.igst_amount AS TEXT) AS igstAmt,
//            items.cess_rate AS itemsCessRate,
//            CAST(items.cess_amount AS TEXT) AS cessAmt,
//            items.additional_cess_rate AS itemsAdditionalCessRate,
//            CAST(items.additional_cess_amount AS TEXT) AS addCessAmt,
//            IFNULL(items.uom, '') AS itemsUom,
//            CASE
//                WHEN items.uom = 'PC' THEN items.quantity
//                ELSE items.quantity / 1000.0
//            END AS itemsQuantityAdjusted,
//            items.total_amount AS totalPaidAmount,
//            invoices.MDR_VALUE AS mdrValue,
//            invoices.is_credit AS invoicesIsCredit,
//            invoices.customer_phone AS invoicesCustomerPhone,
//            invoices.total_amount AS invoiceTotalAmount,
//            invoices.total_discount AS totalDiscount,
//            invoices.total_vat_amount AS totalVatAmount,
//            invoices.total_sgst_amount AS totalSgstAmount,
//            invoices.total_cgst_amount AS totalCgstAmount,
//            invoices.total_igst_amount AS totalIgstAmount,
//            invoices.total_cess_amount AS totalCessAmount,
//            invoices.total_additional_cess_amount AS totalAdditionalCessAmount
//        FROM invoices
//        LEFT JOIN items ON invoices._id = items.invoice_id
//        LEFT JOIN customers ON invoices.customer_phone = customers.phone
//        LEFT JOIN transactions ON invoices._id = transactions.INVOICE_ID
//        WHERE invoices.created_at >= :startDate
//          AND invoices.created_at <= :endDate
//        ORDER BY invoices.created_at ASC
//    """
//    )
//
//    suspend fun getSalesReport(startDate: Long, endDate: Long): List<SalesReportItem>

    @Query("UPDATE INVOICES SET IS_HPH_SYNC_PENDING = :success WHERE _id = :invoiceId")
    suspend fun markHPHStatus(invoiceId: Long, success: Boolean)
}
