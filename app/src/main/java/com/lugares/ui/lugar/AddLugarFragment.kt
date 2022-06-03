package com.lugares.ui.lugar

import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lugares.R
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {

    private var _binding: FragmentLugarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var lugarViewModel: LugarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentLugarBinding.inflate(inflater, container, false)
        val root: View = binding.root

      binding.btAgregar.seOnClickListener{
            addLugar()
        }
        return root

    }

    private fun addLugar() {
        val nombre =  binding.etNombre.text.toString
        val correo =  binding.etCorreo.text.toString
        val telefono =  binding.etTelefono.text.toString
        val web =  binding.etWeb.text.toString
        if(nombre.isNotEmpty()) {
            //Si puedo crear un lugar
            val lugar = Lugar(0,nombre,correo,telefono,web,0.0,"","","","")
            lugarViewModel.addLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_added),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addLugarFrag_to_nav_lugar)
        }else{
            //Mensaje de error
            Toast.makeText(requireContext(),getString(R.string.msg_data),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}