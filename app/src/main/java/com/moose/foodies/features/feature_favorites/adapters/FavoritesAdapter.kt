package com.moose.foodies.features.feature_favorites.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.moose.foodies.databinding.FavoriteItemBinding
import com.moose.foodies.features.feature_favorites.adapters.FavoritesAdapter.FavoritesViewHolder
import com.moose.foodies.features.feature_home.domain.Recipe
import com.moose.foodies.util.extensions.loadImage
import com.moose.foodies.util.extensions.mediumImage
import com.moose.foodies.util.extensions.shareRecipe

class FavoritesAdapter(
    private val recipes: List<Recipe>,
    val removeFavorite: (index: Int) -> Unit,
    val showRecipe: (id: Int) -> Unit,
) : Adapter<FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val recipe = recipes[position]

        with(holder.binding){
            val url = recipe.info.image.mediumImage()
            recipeImage.loadImage(url)
        }

        holder.binding.imageCard.setOnClickListener {
            showRecipe(recipe.id)
        }

        holder.binding.delete.setOnClickListener {
            removeFavorite(position)
        }

        holder.binding.share.setOnClickListener {
            it.context.shareRecipe(recipe.info.title, recipe.id)
        }
    }

    override fun getItemCount(): Int = recipes.size

    class FavoritesViewHolder(val binding: FavoriteItemBinding): ViewHolder(binding.root)

}