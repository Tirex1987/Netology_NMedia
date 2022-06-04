package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.data.Post


fun Cursor.toPost() = Post(
    id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
    author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
    content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
    published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
    likes = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
    likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
    share = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARE.columnName)),
    views = getInt(getColumnIndexOrThrow(PostsTable.Column.VIEWS.columnName)),
    videoUrl = if (!isNull(getColumnIndexOrThrow(PostsTable.Column.VIDEO_URL.columnName))) {
        getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO_URL.columnName))
    } else null,
    videoTitle = if (!isNull(getColumnIndexOrThrow(PostsTable.Column.VIDEO_TITLE.columnName))) {
        getString(getColumnIndexOrThrow(PostsTable.Column.VIDEO_TITLE.columnName))
    } else null

)