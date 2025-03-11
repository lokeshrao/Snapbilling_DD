package com.snapbizz.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "CATEGORIES")
data class Category(
    @PrimaryKey @ColumnInfo("_id") @SerializedName("id") var id: Int,
    @ColumnInfo("NAME") @SerializedName("name") var name: String? = null,
    @ColumnInfo("PARENT_ID") @SerializedName("parent_id") var parentId: Int? = null,
    @ColumnInfo("IS_QUICK_ADD") @SerializedName("is_quick_add") var isQuickAdd: Boolean? = null,
    @ColumnInfo("IMAGE_URL") @SerializedName("image_url") var imageUrl: String? = null

){
//    fun getImage(): String {
//        return "$SNAP_IMAGE_TEST_HOST_URL/$SNAP_IMAGE_CONTAINER/SnapImages/category-images/$id.jpg"
//    }
}