package com.listadecompra

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // Variables para el ListView y su adaptador
    private lateinit var listView: ListView
    private lateinit var productoAdapter: ProductoAdapter
    private val productos = mutableListOf<Producto>() // Lista mutable de productos

    // Variables para los campos de entrada y el botón
    private lateinit var editNombre: EditText
    private lateinit var editCantidad: EditText
    private lateinit var editPrecio: EditText
    private lateinit var btnAddProducto: Button

    // Variables para la cabecera
    private lateinit var tvResumen: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilitar diseño de borde a borde
        setContentView(R.layout.activity_main) // Configurar la vista

        // Manejo de insets para compatibilidad visual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom) // Ajustar el padding
            insets
        }

        // Configurar el ListView y el adaptador personalizado
        listView = findViewById(R.id.listView)
        productoAdapter = ProductoAdapter(this, productos) // Inicializar el adaptador con la lista de productos
        listView.adapter = productoAdapter // Asignar el adaptador al ListView

        // Registrar el ListView para el menú contextual
        registerForContextMenu(listView)

        // Referenciar los campos de entrada y el botón
        editNombre = findViewById(R.id.editNombre)
        editCantidad = findViewById(R.id.editCantidad)
        editPrecio = findViewById(R.id.editPrecio)
        btnAddProducto = findViewById(R.id.btnAddProducto)

        // Referenciar la cabecera
        tvResumen = findViewById(R.id.tvResumen)

        // Configurar acción del botón "Añadir producto"
        btnAddProducto.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val cantidad = editCantidad.text.toString().trim()
            val precio = editPrecio.text.toString().trim()

            // Validar que el nombre no esté vacío
            if (nombre.isBlank()) {
                Toast.makeText(this, "El nombre del producto es obligatorio", Toast.LENGTH_SHORT).show()
            } else {
                agregarProducto(nombre, cantidad, precio) // Agregar el producto a la lista
                productoAdapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
                actualizarCabecera() // Actualizar la cabecera

                // Limpiar los campos después de añadir
                editNombre.text.clear()
                editCantidad.text.clear()
                editPrecio.text.clear()
            }
        }
    }

    // Función para agregar productos a la lista
    private fun agregarProducto(nombre: String, cantidad: String, precio: String) {
        productos.add(Producto(nombre, cantidad, precio)) // Añadir un nuevo producto a la lista
    }

    // Función para actualizar la cabecera
    private fun actualizarCabecera() {
        val totalProductos = productos.size
        var totalPrecio = 0f

        for (producto in productos) {
            val cantidadInt = producto.cantidad.toIntOrNull() ?: 1 // Si no hay cantidad, asume 1
            val precioFloat = producto.precio.toFloatOrNull()
            if (precioFloat != null) {
                totalPrecio += precioFloat * cantidadInt // Multiplica precio por cantidad
            }
        }

        tvResumen.text = "$totalProductos productos - Total: ${"%.2f".format(totalPrecio)} €"
    }


    // Función para eliminar un producto
    private fun eliminarProducto(position: Int) {
        if (position in 0 until productos.size) {
            productos.removeAt(position) // Eliminar el producto de la lista
            productoAdapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
            actualizarCabecera() // Actualizar la cabecera
        }
    }

    // Crear el menú contextual
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_contextual, menu) // Infla el menú contextual
    }

    // Manejar las acciones del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo // Obtener la información del menú contextual
        val position = info.position // Posición del producto seleccionado
        when (item.itemId) {
            R.id.itemEliminar -> {
                eliminarProducto(position) // Eliminar el producto
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Guardar la lista de productos como una lista de strings concatenados
        val productosString = productos.map { "${it.nombre},${it.cantidad},${it.precio}" }
        outState.putStringArrayList("productos", ArrayList(productosString)) // Guardar la lista de productos
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Recuperar la lista de productos desde los strings concatenados
        val productosString = savedInstanceState.getStringArrayList("productos")

        productos.clear() // Limpiar la lista actual
        productosString?.forEach { productoStr ->
            val partes = productoStr.split(",")
            if (partes.size == 3) {
                productos.add(Producto(partes[0], partes[1], partes[2])) // Reconstruir los productos
            }
        }

        productoAdapter.notifyDataSetChanged() // Notificar al adaptador de los cambios
        actualizarCabecera() // Actualizar la cabecera
    }


}
