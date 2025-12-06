package com.jbrenorv.acall.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jbrenorv.acall.core.designsystem.theme.ACallTheme

@Composable
internal fun LoginRoute(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // Show credentials bottom sheet
        viewModel.loginScreenStarted(context)
    }

    LoginScreen(
        modifier = modifier,
        isLoading = isLoading,
        onGoogleLoginClick = {
            viewModel.loginWithGoogle(context)
        }
    )
}

@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onGoogleLoginClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            onClick = onGoogleLoginClick,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentDescription = "Login icon"
                )
                Text(
                    text = "Login with Google",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(0.dp, 6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    ACallTheme {
        LoginScreen(
            isLoading = false,
            onGoogleLoginClick = {}
        )
    }
}
