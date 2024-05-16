package com.my.composenews.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.my.composenews.R
import com.my.composenews.domain.vo.NewsVo

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    item: NewsVo = NewsVo()
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .padding(8.dp, 0.dp)
                .clip(RoundedCornerShape(10.dp))
                .shadow(elevation = 10.dp)
        ) {
            if (item.urlToImage.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.mipmap.iv_default),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .fillMaxSize()
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = item.urlToImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Text(
            text = item.title,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Justify,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        )
    }
}


@Preview
@Composable
fun PreviewContent() {
    Surface {
        MainContent(
            item = NewsVo(
                title = "Today is 2024",
                urlToImage = ""
            )
        )
    }
}