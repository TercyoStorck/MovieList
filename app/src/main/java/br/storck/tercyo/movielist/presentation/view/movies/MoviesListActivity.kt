package br.storck.tercyo.movielist.presentation.view.movies

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.storck.tercyo.movielist.R
import br.storck.tercyo.movielist.databinding.ActivityMoviesBinding
import br.storck.tercyo.movielist.presentation.view.moviedetail.MovieDetailActivity
import br.storck.tercyo.movielist.presentation.viewmodel.moviedetail.MovieDetailViewModelFactory
import br.storck.tercyo.movielist.presentation.viewmodel.movielist.MovieListViewModel
import br.storck.tercyo.movielist.presentation.viewmodel.movielist.MovieListViewModelFactory
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this, MovieListViewModelFactory())[MovieListViewModel::class.java]

        DataBindingUtil.setContentView<ActivityMoviesBinding>(
            this, R.layout.activity_movies
        ).let { binding ->
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.navView.setOnNavigationItemSelectedListener { menuItem ->
                viewModel.selectedMenuItem.value = menuItem.itemId
                true
            }
            viewModel.fabIcon.observe(this, Observer {
                binding.fabFilter.setImageResource(it)
            })

            binding
        }

        viewModel.selectedMenuItem.observe(this, Observer {
            this.showList(it)
        })

        viewModel.selectedMovie.observe(this, Observer {
            it?.let {
                MovieDetailViewModelFactory.movie = it

                val intent = Intent(this, MovieDetailActivity::class.java)
                this.startActivity(intent)
            }
        })

        this.createFragments()
    }

    private fun createFragments() {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()

        val homeFragment = MovieListFragment()
        val favoritesFragment = MovieListFragment()

        fragmentTransaction.add(R.id.frame_content,  homeFragment)
        fragmentTransaction.add(R.id.frame_content,  favoritesFragment)
        fragmentTransaction.hide(favoritesFragment)
        fragmentTransaction.commit()
    }

    private fun showList(fragmentId: Int) {
        val index = when (fragmentId) {
            R.id.menu_favorites -> 1
            else -> 0
        }

        val fragment = if (this.supportFragmentManager.fragments.size > index) this.supportFragmentManager.fragments[index] else null
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()

        fragment?.let { fragmentToShow ->
            this.supportFragmentManager.fragments.forEach {
                fragmentTransaction.hide(it)
            }

            fragmentTransaction.show(fragmentToShow)
            fragmentTransaction.commit()
        } ?: run {
            fragmentTransaction.add(R.id.frame_content,  MovieListFragment())
            fragmentTransaction.commit()
        }
    }
}
