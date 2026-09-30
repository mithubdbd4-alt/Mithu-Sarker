package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CalendarEvent
import com.example.ui.CalendarDialogState
import com.example.ui.CalendarViewModel
import com.example.ui.components.AddEditEventDialog
import com.example.ui.components.AddEditNoteDialog
import com.example.ui.components.CalendarHeader
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.DashboardSummaryCards
import com.example.ui.components.ExportImportDialog
import com.example.ui.components.MonthCalendarView
import com.example.ui.components.SearchSection
import com.example.ui.components.SelectedDateSection
import com.example.ui.components.UpcomingEventsSection
import com.example.ui.components.ViewEventDialog
import com.example.util.DateUtils
import com.example.util.PrintHelper
import com.example.util.Strings
import java.time.LocalDate

@Composable
fun CalendarMainScreen(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val today by viewModel.today.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val displayedMonth by viewModel.displayedMonth.collectAsStateWithLifecycle()
    val allEvents by viewModel.allEvents.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CalendarHeader(
                language = language,
                isDarkMode = isDarkMode,
                isSearchActive = isSearchActive,
                onToggleSearch = { viewModel.toggleSearch(it) },
                onToggleTheme = { viewModel.toggleDarkMode() },
                onToggleLanguage = { viewModel.toggleLanguage() },
                onPrintClicked = {
                    PrintHelper.printCalendar(
                        context = context,
                        yearMonth = displayedMonth,
                        events = allEvents,
                        notes = allNotes,
                        language = language
                    )
                },
                onExportImportClicked = { viewModel.openExportImport() },
                onRestoreSampleData = {
                    viewModel.restoreSampleData()
                    viewModel.showSnackbar("Sample data restored successfully!")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddEvent(selectedDate) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("main_add_event_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = Strings.addEvent(language))
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search Section if active
                if (isSearchActive) {
                    Spacer(modifier = Modifier.height(6.dp))
                    SearchSection(
                        searchQuery = searchQuery,
                        searchResults = searchResults,
                        language = language,
                        onQueryChanged = { viewModel.setSearchQuery(it) },
                        onClearQuery = { viewModel.setSearchQuery("") },
                        onEventClicked = { event ->
                            DateUtils.parseDate(event.date)?.let { viewModel.selectDate(it) }
                            viewModel.openViewEvent(event)
                        }
                    )
                }

                // Dashboard Summary Cards
                DashboardSummaryCards(
                    events = allEvents,
                    today = today,
                    language = language,
                    onTodayCardClicked = { viewModel.goToToday() },
                    onUpcomingCardClicked = { /* Already scrollable */ }
                )

                // Month Calendar Grid
                MonthCalendarView(
                    displayedMonth = displayedMonth,
                    selectedDate = selectedDate,
                    events = allEvents,
                    notes = allNotes,
                    language = language,
                    onPreviousMonth = { viewModel.previousMonth() },
                    onNextMonth = { viewModel.nextMonth() },
                    onTodayClicked = { viewModel.goToToday() },
                    onDateSelected = { date -> viewModel.selectDate(date) }
                )

                // Selected Date Detail Section (Events & Note)
                val selectedDateNote = allNotes.find { it.date == selectedDate.toString() }
                SelectedDateSection(
                    selectedDate = selectedDate,
                    events = allEvents,
                    note = selectedDateNote,
                    language = language,
                    onAddEventClicked = { viewModel.openAddEvent(selectedDate) },
                    onAddEditNoteClicked = { viewModel.openNoteDialog(selectedDate) },
                    onDeleteNoteClicked = { dateIso -> viewModel.confirmDeleteNote(dateIso) },
                    onEventClicked = { event -> viewModel.openViewEvent(event) },
                    onEditEventClicked = { event -> viewModel.openEditEvent(event) },
                    onDeleteEventClicked = { event -> viewModel.confirmDeleteEvent(event) }
                )

                // Upcoming Events Section
                UpcomingEventsSection(
                    events = allEvents,
                    today = today,
                    language = language,
                    onEventClicked = { event ->
                        DateUtils.parseDate(event.date)?.let { viewModel.selectDate(it) }
                        viewModel.openViewEvent(event)
                    }
                )

                Spacer(modifier = Modifier.height(80.dp)) // Extra space for FAB
            }
        }
    }

    // Dialog handlers based on dialogState
    when (val state = dialogState) {
        CalendarDialogState.None -> Unit

        is CalendarDialogState.AddEvent -> {
            AddEditEventDialog(
                initialEvent = null,
                initialDate = state.initialDate,
                language = language,
                onDismiss = { viewModel.dismissDialog() },
                onSave = { id, title, date, startTime, endTime, desc, colorHex, isImportant ->
                    viewModel.saveEvent(
                        id = id,
                        title = title,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        description = desc,
                        colorHex = colorHex,
                        isImportant = isImportant
                    )
                }
            )
        }

        is CalendarDialogState.EditEvent -> {
            val eventDate = DateUtils.parseDate(state.event.date) ?: LocalDate.now()
            AddEditEventDialog(
                initialEvent = state.event,
                initialDate = eventDate,
                language = language,
                onDismiss = { viewModel.dismissDialog() },
                onSave = { id, title, date, startTime, endTime, desc, colorHex, isImportant ->
                    viewModel.saveEvent(
                        id = id,
                        title = title,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        description = desc,
                        colorHex = colorHex,
                        isImportant = isImportant
                    )
                }
            )
        }

        is CalendarDialogState.ViewEvent -> {
            ViewEventDialog(
                event = state.event,
                language = language,
                onDismiss = { viewModel.dismissDialog() },
                onEdit = { viewModel.openEditEvent(state.event) },
                onDelete = { viewModel.confirmDeleteEvent(state.event) }
            )
        }

        is CalendarDialogState.AddEditNote -> {
            AddEditNoteDialog(
                date = state.date,
                existingNote = state.existingNote,
                language = language,
                onDismiss = { viewModel.dismissDialog() },
                onSave = { noteText ->
                    viewModel.saveNote(state.date.toString(), noteText)
                },
                onDelete = {
                    viewModel.deleteNote(state.date.toString())
                }
            )
        }

        is CalendarDialogState.ConfirmDeleteEvent -> {
            ConfirmDeleteDialog(
                title = Strings.confirmDeleteTitle(language),
                message = Strings.confirmDeleteMessage(language),
                language = language,
                onConfirm = { viewModel.deleteEvent(state.event) },
                onDismiss = { viewModel.dismissDialog() }
            )
        }

        is CalendarDialogState.ConfirmDeleteNote -> {
            ConfirmDeleteDialog(
                title = Strings.confirmDeleteTitle(language),
                message = Strings.confirmDeleteMessage(language),
                language = language,
                onConfirm = { viewModel.deleteNote(state.date) },
                onDismiss = { viewModel.dismissDialog() }
            )
        }

        CalendarDialogState.ExportImport -> {
            ExportImportDialog(
                exportJson = viewModel.exportDataJson(),
                language = language,
                onDismiss = { viewModel.dismissDialog() },
                onImport = { json -> viewModel.importDataJson(json) },
                onShowMessage = { msg -> viewModel.showSnackbar(msg) }
            )
        }
    }
}
