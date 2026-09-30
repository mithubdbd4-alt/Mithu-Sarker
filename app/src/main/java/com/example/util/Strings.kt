package com.example.util

object Strings {
    fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বাংলা ক্যালেন্ডার" else "Smart Calendar"
    fun timezoneBadge(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ঢাকা সময় (Asia/Dhaka)" else "Dhaka Time (Asia/Dhaka)"
    
    // Buttons
    fun prevMonth(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পূর্ববর্তী" else "Previous"
    fun nextMonth(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পরবর্তী" else "Next"
    fun today(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আজকে" else "Today"
    fun tomorrow(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আগামীকাল" else "Tomorrow"
    fun nextWeek(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পরবর্তী সপ্তাহ" else "Next Week"
    fun addEvent(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্ট যোগ করুন" else "Add Event"
    fun editEvent(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্ট সম্পাদনা" else "Edit Event"
    fun deleteEvent(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্ট মুছুন" else "Delete Event"
    fun save(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "সংরক্ষণ করুন" else "Save"
    fun cancel(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বাতিল" else "Cancel"
    fun close(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বন্ধ করুন" else "Close"
    fun delete(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মুছুন" else "Delete"
    fun edit(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এডিট" else "Edit"
    
    // Notes
    fun notes(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোট" else "Notes"
    fun addNote(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোট যোগ করুন" else "Add Note"
    fun editNote(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নোট সম্পাদনা" else "Edit Note"
    fun notePlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এই তারিখের বিশেষ কোনো নোট বা টাস্ক লিখুন..." else "Write a note or reminder for this date..."
    fun noNote(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো নোট লেখা হয়নি" else "No note for this date"

    // Dashboard Cards
    fun totalEvents(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মোট ইভেন্ট" else "Total Events"
    fun todayEvents(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আজকের ইভেন্ট" else "Today's Events"
    fun upcomingEvents(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আসন্ন ইভেন্ট" else "Upcoming Events"
    fun importantEvents(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "গুরুত্বপূর্ণ" else "Important"

    // Search
    fun search(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্ট খুঁজুন..." else "Search events..."
    fun searchResults(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অনুসন্ধান ফলাফল" else "Search Results"
    fun noSearchResults(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো ফলাফল পাওয়া যায়নি" else "No events found"
    
    // Dialog labels
    fun eventTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্টের শিরোনাম" else "Event Title"
    fun eventTitlePlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "যেমন: মিটিং, ভ্রমণ, পরীক্ষা..." else "e.g. Meeting, Travel, Birthday..."
    fun date(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "তারিখ" else "Date"
    fun startTime(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "শুরুর সময়" else "Start Time"
    fun endTime(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "শেষের সময়" else "End Time"
    fun description(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "বিবরণ" else "Description"
    fun descriptionPlaceholder(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্টের বিস্তারিত তথ্য এখানে লিখুন..." else "Write event details here..."
    fun eventColor(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইভেন্টের রং নির্বাচন" else "Select Color"
    fun markImportant(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "গুরুত্বপূর্ণ হিসেবে চিহ্নিত করুন" else "Mark as Important"
    fun validationTitleRequired(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "অনুগ্রহ করে ইভেন্টের শিরোনাম দিন!" else "Please enter event title!"

    // Confirmation
    fun confirmDeleteTitle(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "মুছে ফেলার নিশ্চিতকরণ" else "Confirm Deletion"
    fun confirmDeleteMessage(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "আপনি কি নিশ্চিত যে আপনি এটি মুছে ফেলতে চান? এটি পুনরুদ্ধার করা যাবে না।" else "Are you sure you want to delete this? This action cannot be undone."

    // More events badge
    fun more(count: Int, lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "+${LanguageManager.toBengaliDigits(count.toString())} আরও" else "+$count more"
    
    // Top menu options
    fun printCalendar(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ক্যালেন্ডার প্রিন্ট" else "Print Calendar"
    fun exportData(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডাটা এক্সপোর্ট (JSON)" else "Export Data (JSON)"
    fun importData(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডাটা ইমপোর্ট (JSON)" else "Import Data (JSON)"
    fun resetSampleData(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "নমুনা ইভেন্ট রিস্টোর" else "Restore Sample Data"
    
    // Export/Import
    fun exportSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডাটা সফলভাবে ক্লিপবোর্ডে কপি হয়েছে!" else "Data copied to clipboard successfully!"
    fun importInstruction(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "পূর্বে এক্সপোর্ট করা JSON কোড নিচে পেস্ট করুন:" else "Paste previously exported JSON code below:"
    fun importButton(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ইমপোর্ট করুন" else "Import Now"
    fun importSuccess(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ডাটা সফলভাবে ইমপোর্ট করা হয়েছে!" else "Data successfully imported!"
    fun importError(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "ভুল ফরম্যাট! অনুগ্রহ করে সঠিক JSON ডাটা দিন।" else "Invalid format! Please provide valid JSON."

    // Empty states
    fun noEventsForDate(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "এই তারিখে কোনো ইভেন্ট নির্ধারিত নেই।" else "No events scheduled for this date."
    fun noUpcomingEvents(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "কোনো আসন্ন ইভেন্ট নেই।" else "No upcoming events."
    fun quickAddHint(lang: AppLanguage) = if (lang == AppLanguage.BANGLA) "একটি নতুন ইভেন্ট যোগ করতে উপরের বাটনে চাপুন।" else "Tap the button above to schedule an event."
}
