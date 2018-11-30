var today = new Date();
var month = today.getMonth();


//Using the Date prototype to assign our month names-->
Date.prototype.getMonthNames = function() { return [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ]; }
//Getting the number of day in the month.-->
Date.prototype.getDaysInMonth = function()
{ return new Date( this.getFullYear(), this.getMonth() + 1, 0 ).getDate(); }
Date.prototype.calendar = function() {
    var numberOfDays = this.getDaysInMonth();
    //This will be the starting day to our calendar-->
    var startingDay = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
    //We will build the calendar_table variable then pass what we build back-->
    var calendarTable = '<div id="month">' + this.getMonthNames()[this.getMonth()] + '&nbsp;' + this.getFullYear() + '</div>';
    calendarTable += '<table summary="Calendar" class="calendar" style="text-align: center;">';
    calendarTable += '<tr><td colspan="7"></td></tr>';
    calendarTable += '<tr>';
    calendarTable += '<td class="titleColumn">SUN</font></td>';
    calendarTable += '<td class="titleColumn">MON</td>';
    calendarTable += '<td class="titleColumn">TUES</td>';
    calendarTable += '<td class="titleColumn">WED</td>';
    calendarTable += '<td class="titleColumn">THURS</td>';
    calendarTable += '<td class="titleColumn">FRI</td>';
    calendarTable += '<td class="titleColumn">SAT</td></tr>';
    //Lets create blank boxes until we get to the day which actually starts the month-->
    for ( var i = 0; i < startingDay; i++ )
    { calendarTable += '<td class="dayBlock">&nbsp;</td>'; }
    //border is a counter, initialize it with the "blank"-->
    //days at the start of the calendar-->
    //Now each time we add new date we will do a modulus-->
    //7 and check for 0 (remainder of border/7 = 0)
    //if it's zero then it's time to make new row-->
    var border = startingDay;
    //For each day in the month, insert it into the calendar-->
    for ( var id = '',  i = 1; i <= numberOfDays; i++ )
    { if (( month == this.getMonth() ) && ( today.getDate() == i )) { id = 'id="current_day"'; }
    else { id = ''; }
        calendarTable += '<td class="dayBlock" ' + id + '>' + i +'</td>'; border++;
        if ((( border % 7 ) == 0 ) && ( i < numberOfDays ))
        {
            //Time to make new row, if there are any days left.-->
            calendarTable += '<tr></tr>'; } }
    //All the days have been used up, so pad the empty days until the end of calendar-->
    while(( border++ % 7)!= 0)
    { calendarTable += '<td>&nbsp;</td>'; }
    //Finish the table-->
    calendarTable += '</table>';
    //Return it-->
    return calendarTable; }
//--> Let's add up some dynamic effect
window.onload = function() {
    selected_month = '<form name="month_holder">';
    selected_month += '<select id="month_items" size="1" onchange="month_picker();">';
    for ( var x = 0; x <= today.getMonthNames().length; x++ ) { selected_month += '<option value="' + today.getMonthNames()[x] + ' 1, ' + today.getFullYear() + '">' + today.getMonthNames()[x] + '</option>'; }
    selected_month += '</select></form>';
    actual_calendar = document.getElementById('show_calendar');
    actual_calendar.innerHTML = today.calendar();
    var month_listing = document.getElementById('current_month');
    month_listing.innerHTML = selected_month;
    actual_month = document.getElementById('month_items');
    actual_month.selectedIndex = month;
}
//--> Month Picker <--\\
function month_picker()
{ month_menu = new Date(actual_month.value);
    actual_calendar.innerHTML = month_menu.calendar();
}
// Done Hiding -->
