package com.numq.firebasechat.home.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DrawerItem(article: DrawerArticle, onItemClick: (DrawerArticle) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onItemClick(article)
        }) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = article.icon, contentDescription = "")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = article.title, fontWeight = FontWeight.Medium)
        }
    }
}