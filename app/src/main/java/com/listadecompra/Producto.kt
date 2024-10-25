package com.listadecompra

// Clase de datos para representar un producto
data class Producto(
    val nombre: String, // Nombre del producto
    val cantidad: String = "", // Cantidad del producto (opcional)
    val precio: String = "" // Precio del producto (opcional)
)
