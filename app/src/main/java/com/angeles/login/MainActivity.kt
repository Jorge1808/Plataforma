package com.angeles.login

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer:DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var botonUrl: Button


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar:Toolbar=findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer=findViewById(R.id.drawer_layout)
        botonUrl=findViewById(R.id.urlButton)

        toggle= ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView:NavigationView=findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        botonUrl.setOnClickListener{
            val url = "https://gestion.pe/economia/empresas/ahorra-food-depot-debuta-en-peru-en-julio-2025-un-nuevo-rival-para-mass-y-3a-de-aje-tiendas-de-descuento-intercorp-supermercados-mayoristas-tiendas-de-conveniecia-noticia/" // Reemplaza con la URL deseada
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            finish()
        }

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
         R.id.item1 -> Toast.makeText(this,"Item1",Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return true

    }
    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}