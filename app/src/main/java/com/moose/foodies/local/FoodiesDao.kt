package com.moose.foodies.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.moose.foodies.features.feature_home.domain.HomeData
import com.moose.foodies.features.feature_home.domain.Recipe
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FoodiesDao{
    @Query("SELECT * FROM recipes")
    fun getHomeData(): Flowable<HomeData>

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Single<List<Recipe>>

    @Query("SELECT COUNT(*) FROM favorites")
    fun getFavoritesCount(): Single<Int>

    @Query("SELECT * FROM favorites WHERE id LIKE :id")
    fun getFavoriteById(id: Int): Single<Recipe>

    @Insert(onConflict = REPLACE)
    fun updateHomeData(data: HomeData): Completable

    @Insert(onConflict = REPLACE)
    fun insertFavorite(recipe: Recipe): Completable

    @Insert(onConflict = REPLACE)
    fun insertFavorites(recipes: List<Recipe>): Completable

    @Query("DELETE FROM recipes")
    fun deleteRecipes(): Completable

    @Query("DELETE FROM favorites WHERE id = :id")
    fun deleteFavorite(id: Int): Completable
}   