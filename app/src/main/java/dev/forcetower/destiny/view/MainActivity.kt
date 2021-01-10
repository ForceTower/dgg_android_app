package dev.forcetower.destiny.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.destiny.R
import dev.forcetower.destiny.databinding.ActivityMainBinding
import dev.forcetower.toolkit.components.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val navController
        get() = findNavController(R.id.fragment_container)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun showSnack(string: String, duration: Int) {
        getSnackInstance(string, duration)?.show()
    }

    override fun getSnackInstance(string: String, duration: Int): Snackbar? {
        return Snackbar.make(binding.root, string, duration)
    }
}