/*document.addEventListener('visibilitychange', handleVisibilityChange);

function handleVisibilityChange() {
    if (document.hidden) {
        // Document is hidden, screen is likely asleep
        console.log('Screen asleep');
    } else {
        // Document is visible, screen is active
        console.log('Screen active');
    }
}
document.addEventListener('DOMContentLoaded', (event) => {
    console.log('JavaScript file loaded and DOM is fully loaded');

    let clockingId = document.getElementById('clockingId').value;
    let userId = document.getElementById('userId').value;

    document.getElementById('clockInButton').addEventListener('click', () => clockIn(userId));
    document.getElementById('clockOutButton').addEventListener('click', clockOut);

    // Function to handle screen sleep (visibility change to hidden)
    function handleVisibilityChange() {
        if (document.hidden) {
            // Screen is asleep
            console.log('Screen asleep');
            if (clockingId) {
                console.log("pausing clockingId:", clockingId);
                pauseClocking(userId);
            }
        } else {
            // Screen is resumed
            console.log('Screen resumed');
            if (clockingId) {
                console.log("resuming clockingId:", clockingId);
                resumeClocking(userId);
            }
        }
    }

    // Add event listener for visibility change
    document.addEventListener('visibilitychange', handleVisibilityChange);

    window.addEventListener('beforeunload', () => {
        if (clockingId) {
            console.log("pausing clockingId:", clockingId);
            pauseClocking(userId);
        }
    });

    window.addEventListener('focus', () => {
        if (clockingId) {
            console.log("resuming clockingId:", clockingId);
            resumeClocking(userId);
        }
    });

    function clockIn(userId) {
        fetch(`/api/clockings/clock-in/${userId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            console.log('Clock In Success:', data);
            document.getElementById('clockingId').value = data.id;
            document.getElementById('clockInTime').innerText = data.clockInDate;
            document.getElementById('clockOutTime').innerText = '';
            clockingId = data.id;
        })
        .catch(error => console.error('Error:', error));
    }

    function clockOut() {
        fetch(`/api/clockings/clock-out/${clockingId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            console.log('Clock Out Success:', data);
            document.getElementById('clockOutTime').innerText = data.clockOutDate;
            clockingId = null;
            document.getElementById('clockingId').value = '';
        })
        .catch(error => console.error('Error:', error));
    }

    function pauseClocking(userId) {
        fetch(`/api/clockings/pause`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userId: userId }),
        })
        .then(response => {
            if (response.ok) {
                console.log('Pause Success');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function resumeClocking(userId) {
        fetch(`/api/clockings/resume`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userId: userId }),
        })
        .then(response => {
            if (response.ok) {
                console.log('Resume Success');
            }
        })
        .catch(error => console.error('Error:', error));
    }
});
*/