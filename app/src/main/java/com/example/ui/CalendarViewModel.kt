package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CalendarDatabase
import com.example.data.CalendarEvent
import com.example.data.CalendarRepository
import com.example.data.DateNote
import com.example.util.AppLanguage
import com.example.util.DateUtils
import com.example.util.JsonHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

sealed interface CalendarDialogState {
    data object None : CalendarDialogState
    data class AddEvent(val initialDate: LocalDate) : CalendarDialogState
    data class EditEvent(val event: CalendarEvent) : CalendarDialogState
    data class ViewEvent(val event: CalendarEvent) : CalendarDialogState
    data class AddEditNote(val date: LocalDate, val existingNote: String) : CalendarDialogState
    data class ConfirmDeleteEvent(val event: CalendarEvent) : CalendarDialogState
    data class ConfirmDeleteNote(val date: String) : CalendarDialogState
    data object ExportImport : CalendarDialogState
}

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("calendar_prefs", Context.MODE_PRIVATE)
    private val repository: CalendarRepository

    init {
        val db = CalendarDatabase.getDatabase(application)
        repository = CalendarRepository(db.calendarDao())
    }

    private val _language = MutableStateFlow(
        if (prefs.getString("pref_lang", "bn") == "en") AppLanguage.ENGLISH else AppLanguage.BANGLA
    )
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isDarkMode = MutableStateFlow(
        prefs.getBoolean("pref_dark_mode", false)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _today = MutableStateFlow(DateUtils.getTodayInDhaka())
    val today: StateFlow<LocalDate> = _today.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtils.getTodayInDhaka())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _displayedMonth = MutableStateFlow(YearMonth.from(DateUtils.getTodayInDhaka()))
    val displayedMonth: StateFlow<YearMonth> = _displayedMonth.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _dialogState = MutableStateFlow<CalendarDialogState>(CalendarDialogState.None)
    val dialogState: StateFlow<CalendarDialogState> = _dialogState.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    val allEvents: StateFlow<List<CalendarEvent>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<DateNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered search results
    val searchResults: StateFlow<List<CalendarEvent>> = combine(allEvents, _searchQuery) { events, query ->
        if (query.isBlank()) {
            emptyList()
        } else {
            val q = query.trim().lowercase()
            events.filter {
                it.title.lowercase().contains(q) ||
                        it.description.lowercase().contains(q) ||
                        it.date.contains(q)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Pre-populate demo data if first launch
        viewModelScope.launch {
            val hasLaunchedBefore = prefs.getBoolean("has_launched_before", false)
            if (!hasLaunchedBefore) {
                repository.populateSampleDataIfEmpty()
                prefs.edit().putBoolean("has_launched_before", true).apply()
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        // If clicking on a date of a different month, switch displayed month
        if (YearMonth.from(date) != _displayedMonth.value) {
            _displayedMonth.value = YearMonth.from(date)
        }
    }

    fun previousMonth() {
        _displayedMonth.value = _displayedMonth.value.minusMonths(1)
    }

    fun nextMonth() {
        _displayedMonth.value = _displayedMonth.value.plusMonths(1)
    }

    fun goToToday() {
        val todayDate = DateUtils.getTodayInDhaka()
        _today.value = todayDate
        _selectedDate.value = todayDate
        _displayedMonth.value = YearMonth.from(todayDate)
    }

    fun toggleLanguage() {
        val newLang = if (_language.value == AppLanguage.BANGLA) AppLanguage.ENGLISH else AppLanguage.BANGLA
        _language.value = newLang
        prefs.edit().putString("pref_lang", newLang.code).apply()
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        prefs.edit().putString("pref_lang", lang.code).apply()
    }

    fun toggleDarkMode() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        prefs.edit().putBoolean("pref_dark_mode", newMode).apply()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSearch(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    // Dialog controllers
    fun openAddEvent(date: LocalDate = _selectedDate.value) {
        _dialogState.value = CalendarDialogState.AddEvent(date)
    }

    fun openEditEvent(event: CalendarEvent) {
        _dialogState.value = CalendarDialogState.EditEvent(event)
    }

    fun openViewEvent(event: CalendarEvent) {
        _dialogState.value = CalendarDialogState.ViewEvent(event)
    }

    fun openNoteDialog(date: LocalDate = _selectedDate.value) {
        val dateStr = DateUtils.formatDateIso(date)
        val existing = allNotes.value.find { it.date == dateStr }?.note ?: ""
        _dialogState.value = CalendarDialogState.AddEditNote(date, existing)
    }

    fun confirmDeleteEvent(event: CalendarEvent) {
        _dialogState.value = CalendarDialogState.ConfirmDeleteEvent(event)
    }

    fun confirmDeleteNote(date: String) {
        _dialogState.value = CalendarDialogState.ConfirmDeleteNote(date)
    }

    fun openExportImport() {
        _dialogState.value = CalendarDialogState.ExportImport
    }

    fun dismissDialog() {
        _dialogState.value = CalendarDialogState.None
    }

    // CRUD
    fun saveEvent(
        id: Long = 0,
        title: String,
        date: String,
        startTime: String,
        endTime: String,
        description: String,
        colorHex: String,
        isImportant: Boolean
    ) {
        viewModelScope.launch {
            val event = CalendarEvent(
                id = id,
                title = title.trim(),
                date = date,
                startTime = startTime.trim(),
                endTime = endTime.trim(),
                description = description.trim(),
                colorHex = colorHex,
                isImportant = isImportant
            )
            if (id == 0L) {
                repository.insertEvent(event)
            } else {
                repository.updateEvent(event)
            }
            dismissDialog()
        }
    }

    fun deleteEvent(event: CalendarEvent) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            dismissDialog()
        }
    }

    fun saveNote(date: String, note: String) {
        viewModelScope.launch {
            repository.saveNote(date, note)
            dismissDialog()
        }
    }

    fun deleteNote(date: String) {
        viewModelScope.launch {
            repository.deleteNote(date)
            dismissDialog()
        }
    }

    fun exportDataJson(): String {
        return JsonHelper.exportToJson(allEvents.value, allNotes.value)
    }

    fun importDataJson(json: String): Boolean {
        val parsed = JsonHelper.parseFromJson(json) ?: return false
        viewModelScope.launch {
            if (parsed.events.isNotEmpty()) {
                repository.insertEvents(parsed.events)
            }
            if (parsed.notes.isNotEmpty()) {
                repository.insertNotes(parsed.notes)
            }
        }
        return true
    }

    fun restoreSampleData() {
        viewModelScope.launch {
            repository.clearAllEvents()
            repository.clearAllNotes()
            repository.populateSampleDataIfEmpty()
        }
    }
}
