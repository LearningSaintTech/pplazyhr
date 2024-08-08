var startTime;
var timer;
var elapsedBeforePause = 0;
var running = false;

function formatDate(timestamp) {
    console.log("1");
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Months are 0-based
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function startTimer() {
    console.log("2");
    startTime = new Date().getTime(); // Record the start time
    localStorage.setItem('startTime', startTime); // Save the start time to local storage
    timer = setInterval(updateTimer, 1000);
    running = true;
    localStorage.setItem('running', running);
}

function updateTimer() {
    console.log("3");
    var now = new Date().getTime();
    var elapsed = elapsedBeforePause + (now - startTime);

    var hours = Math.floor((elapsed % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    var minutes = Math.floor((elapsed % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((elapsed % (1000 * 60)) / 1000);

    // Ensure hours, minutes, and seconds are always two digits
    hours = (hours < 10 ? '0' : '') + hours;
    minutes = (minutes < 10 ? '0' : '') + minutes;
    seconds = (seconds < 10 ? '0' : '') + seconds;

    document.getElementById('update').innerHTML = hours + ':' + minutes + ':' + seconds;
}

function pauseTimer() {
    console.log("4");
    clearInterval(timer);
    var now = new Date().getTime();
    elapsedBeforePause += (now - startTime);
    localStorage.setItem('elapsedBeforePause', elapsedBeforePause);
    running = false;
    localStorage.setItem('running', running);
}

function resumeTimer() {
    console.log("5");
    startTime = new Date().getTime();
    timer = setInterval(updateTimer, 1000);
    running = true;
    localStorage.setItem('running', running);
}

async function clockIn(userId) {
    console.log("6", userId);
    try {
        const response = await fetch(`/api/clockings/clock-in/${userId}`, { method: 'POST' });
        const clocking = await response.json();
        const clockInTime = new Date(clocking.clockInDate).toLocaleString();
        const formattedDate = formatDate(clocking.clockInDate);
        localStorage.setItem("clockouttime_", formattedDate);

        const currentclockindate = new Date().toLocaleString();
        localStorage.setItem('currentclockindate', currentclockindate);
        localStorage.setItem('clockInTimestamp', clocking.clockInDate);

        document.getElementById('clockingId').value = clocking.id;
        document.getElementById('clockInTime').innerText = clockInTime;

        localStorage.setItem('clockingId', clocking.id);
        localStorage.setItem('clockInTime', clockInTime);
        localStorage.setItem('clockInUserId', userId);

        document.getElementById('clockInButton').classList.add('disabled'); // Disable Clock In button
        document.getElementById('clockOutButton').classList.remove('disabled'); // Enable Clock Out button
        document.getElementById('clockOutButton').disabled = false; // Enable the button

        document.getElementById('clockInButton').disabled = true; // Disable the button

        startTimer(); // Start the timer when clocking in
    } catch (error) {
        console.error('Error clocking in:', error);
    }
}

async function clockOut() {
    console.log("7");
    const confirmed = confirm("Are you sure you want to clock out?");
    if (!confirmed) {
        return; // Do nothing if the user cancels
    }

    try {
        const clockingId = document.getElementById('clockingId').value || localStorage.getItem('clockingId');
        const response = await fetch(`/api/clockings/clock-out/${clockingId}`, { method: 'POST' });
        const clocking = await response.json();
        const clockOutTime = new Date(clocking.clockOutDate).toLocaleString();
        const formattedDate = formatDate(clocking.clockOutDate);
        localStorage.setItem('clockouttime_', formattedDate);
        document.getElementById('clockOutTime').innerText = clockOutTime;

        localStorage.setItem('clockOutTime', clockOutTime);
        
        // Disable the Clock Out button
        const clockOutButton = document.getElementById('clockOutButton');
        clockOutButton.classList.add('disabled');
        clockOutButton.disabled = true; // Set the disabled attribute
        document.getElementById('clockInButton').disabled = true; // Disable the clock in button

        pauseTimer(); // Pause the timer when clocking out
    } catch (error) {
        console.error('Error clocking out:', error);
    }
}

window.onload = function () {
    console.log("8");
    // Initialize state
    console.log("q", localStorage);

    const startTime = localStorage.getItem('startTime');
    const clockingId = localStorage.getItem('clockingId');
    const clockInTime = localStorage.getItem('clockInTime');
    const clockInUserId = localStorage.getItem('clockInUserId');
    const clockOutTime = localStorage.getItem('clockOutTime');
    var clockInTimestamp = localStorage.getItem('clockInTimestamp');
    elapsedBeforePause = parseInt(localStorage.getItem('elapsedBeforePause')) || 0;
    running = JSON.parse(localStorage.getItem('running')) || false;
    console.log(clockInTimestamp);
    console.log("11111111111111122222222");

    // This line seems for testing purposes. Commented out.
    // clockInTimestamp = "2024-08-06T15:01:40.92112";

    if (clockInTimestamp) {
        const clockInDate = new Date(clockInTimestamp);
        console.log("1q1q", clockInDate);
        const currentDate = new Date();
        const timeDifference = currentDate - clockInDate;
        console.log("111111111111111", timeDifference);
        // Enable buttons 24 hours after clock-in
        if (timeDifference >= 24 * 60 * 60 * 1000) {
            document.getElementById('clockInButton').classList.remove('disabled');
            document.getElementById('clockInButton').disabled = false;
            document.getElementById('clockOutButton').classList.remove('disabled');
            document.getElementById('clockOutButton').disabled = false;
            localStorage.removeItem('clockInTimestamp'); 
            localStorage.clear();
            console.log("q");

            console.log("q", localStorage);

            // Clear the timestamp
        } else {
            // Disable buttons within 24 hours
            document.getElementById('clockInButton').classList.add('disabled');
            document.getElementById('clockInButton').disabled = true;
            document.getElementById('clockOutButton').classList.remove('disabled');
            document.getElementById('clockOutButton').disabled = false;
        }
    }

    if (startTime && clockingId && clockInTime && clockInUserId && !clockOutTime) {
        document.getElementById('clockingId').value = clockingId;
        document.getElementById('clockInTime').innerText = clockInTime;
        document.getElementById('clockInButton').classList.add('disabled'); // Disable clock-in button
        document.getElementById('clockOutButton').classList.remove('disabled'); // Enable clock-out button
        document.getElementById('clockInButton').disabled = true; // Disable the button
        document.getElementById('clockOutButton').disabled = false; // Enable the button

        if (running) {
            resumeTimer();
        }
    } else if (clockOutTime) {
        document.getElementById('clockOutTime').innerText = clockOutTime;
        document.getElementById('clockInTime').innerText = clockInTime; // Keep clock-in time visible
        document.getElementById('clockOutButton').disabled = true; // Disable the clock-out button
        document.getElementById('clockOutButton').classList.add('disabled'); // Disable clock-in button

    } else {
        document.getElementById('clockInButton').classList.remove('disabled'); // Enable clock-in button
        document.getElementById('clockInButton').disabled = false; // Enable the button
        document.getElementById('clockOutButton').classList.add('disabled'); // Disable clock-out button
        document.getElementById('clockOutButton').disabled = true; // Disable the button
    }
};


function generateCalendar() {
    const today = new Date();
    const month = today.getMonth(); // 0-based index
    const year = today.getFullYear();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0).getDate();

    const monthYearElement = document.getElementById('monthYear');
    monthYearElement.textContent = `${getMonthName(month)} ${year}`;

    const calendarBody = document.getElementById('calendarBody');
    calendarBody.innerHTML = '';

    let date = 1;
    for (let i = 0; i < 6; i++) {
        const row = document.createElement('tr');
        for (let j = 0; j < 7; j++) {
            const cell = document.createElement('td');
            if (i === 0 && j < firstDay.getDay()) {
                row.appendChild(cell);
            } else if (date > lastDay) {
                break;
            } else {
                cell.textContent = date;
                if (date === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                    cell.classList.add('today');
                }
                row.appendChild(cell);
                date++;
            }
        }
        calendarBody.appendChild(row);
    }
}

function updateDateTime() {
    console.log("10");
    const dateEl = document.getElementById('current-date');
    const timeEl = document.getElementById('current-time');
    const now = new Date();

    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    dateEl.innerText = now.toLocaleDateString(undefined, options);

    const timeOptions = { hour: '2-digit', minute: '2-digit', second: '2-digit' };
    timeEl.innerText = now.toLocaleTimeString(undefined, timeOptions);
}

updateDateTime();
setInterval(updateDateTime, 1000);

function getMonthName(monthIndex) {
    const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    return months[monthIndex];
}

document.addEventListener('DOMContentLoaded', function () {
    generateCalendar();
});

document.addEventListener("visibilitychange", function () {
    console.log("9");
    if (document.visibilityState === 'hidden') {
        console.log("Page hidden");
        if (running) {
            pauseTimer();
        }
    } else {
        console.log("Page visible again");
        if (running) {
            resumeTimer();
        }
    }
});

// document.getElementById('clockInButton').addEventListener('click', function () {
//     console.log("10");
//     // Make sure user ID is obtained correctly
//     var userId = document.getElementById('clockInButton').dataset.userId;
//     if (userId) {
//         clockIn(userId);
//     } else {
//         console.error('User ID is undefined');
//     }
// });

document.getElementById('clockOutButton').addEventListener('click', function () {
    console.log("11");
    clockOut();
});

// added onclick popup logout

function confirmLogout(event, logoutUrl) {
    event.preventDefault();
    if (confirm("Are you sure you want to logout?")) {
        window.location.href = logoutUrl;
    }
}
