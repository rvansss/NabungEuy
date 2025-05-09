package com.evan0107.nabungeuy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color




@Composable
fun NabungEuyTheme(
    theme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> lightColorScheme(
            primary = Color(0xFFfad59a),
            secondary = Color(0xFFfcefcb)
        )

        AppTheme.DARK -> darkColorScheme()
        AppTheme.BLUE -> lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFF0D47A1)
        )
        AppTheme.GREEN -> lightColorScheme(
            primary = Color(0xFF388E3C),
            secondary = Color(0xFF66BB6A)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
