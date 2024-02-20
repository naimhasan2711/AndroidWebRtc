package com.nakibul.android.androidwebrtc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nakibul.android.androidwebrtc.R
import com.nakibul.android.androidwebrtc.databinding.FragmentLoginBinding
import com.nakibul.android.androidwebrtc.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var mainRepository: MainRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {
            mainRepository.login(
                binding.usernameEt.text.toString(), binding.passwordEt.text.toString()
            ) { isDone, reason ->
                if (!isDone) {
                    Toast.makeText(requireContext(), reason, Toast.LENGTH_SHORT).show()
                } else {
                    //start to the next activity
                    val name = binding.usernameEt.toString()
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment(name)
                    findNavController().navigate(action)
                }

            }
        }
    }

}