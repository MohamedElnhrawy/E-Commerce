package com.gtera.ui.navigation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.gtera.R
import com.gtera.base.BaseActivity
import com.gtera.databinding.BottomNavLayoutBinding
import com.gtera.ui.base.setupWithNavController
import javax.inject.Inject

class BottomNavigationActivity : BaseActivity<BottomNavLayoutBinding, BottomNavViewModel>(),
    BottomNavNavigator {

    @Inject
    lateinit var bottomNavViewModel: BottomNavViewModel
    private var navController: NavController? = null


    private lateinit var navCotroller: NavController
    override fun getLayoutRes(): Int {
        return R.layout.bottom_nav_layout
    }


    override val viewModelClass: Class<BottomNavViewModel>
        get() = BottomNavViewModel::class.java

    override fun setNavigator(viewModel: BottomNavViewModel?) {
        viewModel?.setNavigator(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }

    }

    private fun setupBottomNavigation() {

        // Setup the bottom navigation view with a list of navigation graphs
        var controller: LiveData<NavController> =
            mViewDataBinding.navigationView.setupWithNavController(
                navGraphIds = listOf<Int>(
                    R.navigation.home_nav_graph,
                    R.navigation.search_nav_graph,
                    R.navigation.profile_nav_graph
                ),
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_fragment,
                intent = intent
            )

        controller.observe(this,
            Observer { navController -> this.navController = navController })

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    override fun onShowKeyboard() {
        // do things when keyboard is shown
        mViewDataBinding.navigationView.visibility = View.GONE
    }

    override fun onHideKeyboard() {
        // do things when keyboard is hidden
        mViewDataBinding.navigationView.visibility = View.VISIBLE
    }



    override val isBottomNavigationView: Boolean
        get() = true


    override fun navController(): NavController? {
        return navController
    }

}