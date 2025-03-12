package com.snapbizz.axis.navigation

enum class Screen(val route: String, val title: String) {
    OTP("otp", "OTP Screen"),
    REGISTER("register/{userJson}", "Register Screen"),
    LOGIN("login", "Login Screen"),
    HOME("home", "Home Screen"),
    CART("cart", "Cart"),
    CHECKOUT("checkout", "Checkout"),
    BILLS("bills", "Bills"),
    BILLS_DETAILS("bills_details/{billsJson}", "Bills Details"),
    CUSTOMERS("customers", "Customers"),
    CUSTOMER_DETAILS("customer_details/{customerJson}", "Customer Details"),
    REPORTS("reports", "Reports"),
    INVENTORY("inventory", "Inventory"),
    PAYMENT_TRANSACTIONS("payment_transactions", "Payment Transactions"),
    BACKUP_SUMMARY("backup_summary", "Backup Summary"),
    CONTACT_US("contact_us", "Contact Us"),
    PRINTER_SETTING("printer_settings", "Printer Settings");
  
    companion object {
        fun fromRoute(route: String?): Screen =
            entries.find { it.route == route }
                ?: throw IllegalArgumentException("Invalid route: $route")
    }
}
