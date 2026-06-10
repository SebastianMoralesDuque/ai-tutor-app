package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.room.Room
import com.example.api.RetrofitClient
import com.example.data.database.AppDatabase
import com.example.data.repository.RemoteLearningService
import com.example.data.repository.TutorRepository
import com.example.domain.usecases.GetChatTutorReplyUseCase
import com.example.domain.usecases.GetLessonUseCase
import com.example.domain.usecases.GetStreakUseCase
import com.example.ui.TutorViewModel
import com.example.ui.TutorViewModelFactory
import com.example.ui.screens.ChatTutorScreen
import com.example.ui.screens.DailySessionScreen
import com.example.ui.screens.GoalSetupScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val prefs by lazy {
        getSharedPreferences("tutor_prefs", Context.MODE_PRIVATE)
    }

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tutor_database"
        ).fallbackToDestructiveMigration().build()
    }

    private val repository by lazy { TutorRepository(database, prefs) }

    private val remoteService by lazy {
        RemoteLearningService(
            api = RetrofitClient.api,
            userIdProvider = { repository.getUserId() }
        )
    }

    private val getLessonUseCase by lazy { GetLessonUseCase(remoteService) }
    private val getStreakUseCase by lazy { GetStreakUseCase() }
    private val getChatTutorReplyUseCase by lazy { GetChatTutorReplyUseCase(remoteService) }

    private val viewModel: TutorViewModel by viewModels {
        TutorViewModelFactory(
            repository,
            getLessonUseCase,
            getStreakUseCase,
            getChatTutorReplyUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TutorAppMainView(viewModel)
            }
        }
    }
}

@Composable
fun TutorAppMainView(viewModel: TutorViewModel) {
    val currentProfile by viewModel.userProfile.collectAsState()

    // Default to the Set Goals tab if no active topic profile exists, else start at study tab
    var selectedTab by remember(currentProfile == null) {
        mutableIntStateOf(if (currentProfile == null) 3 else 0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.School, contentDescription = stringResource(R.string.tab_study)) },
                    label = { Text(stringResource(R.string.tab_study)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Chat, contentDescription = stringResource(R.string.tab_tutor)) },
                    label = { Text(stringResource(R.string.tab_tutor)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Timeline, contentDescription = stringResource(R.string.tab_progress)) },
                    label = { Text(stringResource(R.string.tab_progress)) }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Schedule, contentDescription = stringResource(R.string.tab_goals)) },
                    label = { Text(stringResource(R.string.tab_goals)) }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> DailySessionScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                onNavigateToSetup = { selectedTab = 3 },
                onNavigateToTutor = { selectedTab = 1 },
                onNavigateToProgress = { selectedTab = 2 }
            )
            1 -> ChatTutorScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                onNavigateToSetup = { selectedTab = 3 }
            )
            2 -> ProgressScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                onNavigateToSetup = { selectedTab = 3 }
            )
            3 -> GoalSetupScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                onSaveSuccess = { selectedTab = 0 }
            )
        }
    }
}
