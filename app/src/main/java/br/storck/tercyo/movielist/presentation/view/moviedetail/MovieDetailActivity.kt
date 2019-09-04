package br.storck.tercyo.movielist.presentation.view.moviedetail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.storck.tercyo.movielist.R
import br.storck.tercyo.movielist.databinding.ActivityMovieDetailBinding
import br.storck.tercyo.movielist.presentation.viewmodel.moviedetail.MovieDetailViewModel
import br.storck.tercyo.movielist.presentation.viewmodel.moviedetail.MovieDetailViewModelFactory

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this, MovieDetailViewModelFactory())[MovieDetailViewModel::class.java]

        DataBindingUtil.setContentView<ActivityMovieDetailBinding>(
            this, R.layout.activity_movie_detail
        )?.let { binding ->
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
            binding.txvwOverview.movementMethod = ScrollingMovementMethod()

            viewModel.poster?.observe(this, Observer {
                binding.imvwPoster.setImageBitmap(it)
            })
            viewModel.genres?.observe(this, Observer {
                binding.tagGroup.setTags(it)
            })

            binding
        }
    }
}