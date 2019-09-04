package br.storck.tercyo.movielist.presentation.view.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.storck.tercyo.movielist.databinding.ItemMovieListBinding
import br.storck.tercyo.movielist.model.Movie
import br.storck.tercyo.movielist.presentation.viewmodel.movielist.MovieListViewModel
import br.storck.tercyo.movielist.R


class MoviesListAdapter(
    private val owner: LifecycleOwner,
    private val viewModel: MovieListViewModel?
): RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {
    var islastItem: Boolean = false

    init {
        this.viewModel?.movies?.observe(this.owner, Observer {
            this.notifyDataSetChanged()
        })

        this.viewModel?.sortedByDescending?.observe(this.owner, Observer {
            this.islastItem = false
            this.notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = DataBindingUtil.inflate<ItemMovieListBinding>(
            inflater,
            R.layout.item_movie_list,
            viewGroup,
            false
        )

        return ViewHolder(binding, this.viewModel)
    }

    override fun getItemCount(): Int {
        return this.viewModel?.movies?.value?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val movie = this.viewModel?.movies?.value?.get(position)

        viewHolder.itemView.setOnClickListener {
            this.viewModel?.selectedMovie?.value = movie
        }

        this.islastItem = position == this.viewModel?.movies?.value?.lastIndex && !this.islastItem

        viewHolder.bind(movie)
    }

    inner class ViewHolder(
        private val binding: ItemMovieListBinding,
        private val viewModel: MovieListViewModel?
    ): RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(movie: Movie?) {
            this.binding.movie = movie
            this.binding.viewModel = this.viewModel
            this.binding.tagGroup.setTags(movie?.genres?.map {
                it.name
            })
            this.binding.imvwFavorite.setImageResource(if (movie?.favorite != true) R.drawable.ic_favorite_border_black_24dp else R.drawable.ic_favorite_black_24dp)
            this.binding.imvwFavorite.setOnClickListener {
                movie?.let { movie ->
                    this@MoviesListAdapter.viewModel?.manageFavorite(movie)
                    this@MoviesListAdapter.notifyDataSetChanged()
                }
            }
            this.binding.igvwPoster.setImageBitmap(movie?.poster)
            this.binding.executePendingBindings()
        }
    }
}

