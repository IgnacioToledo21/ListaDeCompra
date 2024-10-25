package com.listadecompra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProductoAdapter(context: Context, productos: List<Producto>) :
    ArrayAdapter<Producto>(context, 0, productos) {

    // Método para crear la vista de cada item en el ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView // Reutilizar la vista si es posible
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_producto_horizontal, parent, false) // Inflar nueva vista
        }

        val producto = getItem(position) // Obtener el producto correspondiente a la posición

        // Referenciar los TextViews en el layout del item
        val tvNombre = view!!.findViewById<TextView>(R.id.tvNombre)
        val tvCantidad = view.findViewById<TextView>(R.id.tvCantidad)
        val tvPrecio = view.findViewById<TextView>(R.id.tvPrecio)

        // Asignar el nombre del producto
        tvNombre.text = producto?.nombre ?: "Nombre no especificado"

        // Usar let para acceder a producto de manera segura
        producto?.let {
            tvCantidad.text = if (it.cantidad.isEmpty()) "Cantidad no especificada" else it.cantidad
            tvPrecio.text = if (it.precio.isEmpty()) "Precio no especificado" else "${it.precio} €"
        } ?: run {
            // Si el producto es nulo, mostrar mensajes predeterminados
            tvCantidad.text = "Cantidad no especificada"
            tvPrecio.text = "Precio no especificado"
        }

        return view // Devolver la vista del item
    }
}
