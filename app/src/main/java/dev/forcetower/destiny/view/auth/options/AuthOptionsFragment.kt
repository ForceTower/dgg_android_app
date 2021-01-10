package dev.forcetower.destiny.view.auth.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.destiny.databinding.FragmentAuthOptionsBinding
import dev.forcetower.toolkit.components.BaseFragment
import timber.log.Timber

@AndroidEntryPoint
class AuthOptionsFragment : BaseFragment() {
    private lateinit var binding: FragmentAuthOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Auth options..")
        return FragmentAuthOptionsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.twitch.setOnClickListener {
            findNavController().navigate(AuthOptionsFragmentDirections.actionAuthOptionsToAuthWebView("twitch"))
        }
    }
}