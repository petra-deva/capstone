package com.shiro.fishermanapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.shiro.fishermanapp.R
import com.shiro.fishermanapp.databinding.ActivityMainBinding
import com.shiro.fishermanapp.ui.content.cart.CartFragment
import com.shiro.fishermanapp.ui.content.favorite.FavoriteFragment
import com.shiro.fishermanapp.ui.content.home.HomeFragment
import com.shiro.fishermanapp.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment: Fragment
    private lateinit var auth: FirebaseAuth

    companion object {
        const val DATA = "data"
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extra = intent.extras
        val isSuccess = extra?.getBoolean(DATA)

        if (isSuccess != null && isSuccess)
            Snackbar.make(binding.root, getString(R.string.success), 700).show()

        fragment = HomeFragment.newInstance()
        loadFragment(fragment)

        binding.apply {
            binding.bottomNavigationView.selectedItemId = R.id.bot_nav_placeholder
            bottomNavigationView.setOnNavigationItemSelectedListener {

                when (it.itemId) {
                    R.id.bot_nav_cart -> fragment = CartFragment.newInstance()
                    R.id.bot_nav_favorite -> fragment = FavoriteFragment.newInstance()
                }

                loadFragment(fragment)
            }
            fab.setOnClickListener {
                if (fragment !is HomeFragment) {
                    fragment = HomeFragment.newInstance()
                    loadFragment(fragment)
                }

                binding.bottomNavigationView.selectedItemId = R.id.bot_nav_placeholder
            }
        }

        auth = FirebaseAuth.getInstance()

        btnLogout.setOnClickListener {
            auth.signOut()
            Intent(this@MainActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, fragment)
            .commit()

        return true
    }

}