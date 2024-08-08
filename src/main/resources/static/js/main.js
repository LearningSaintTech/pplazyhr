document.addEventListener("DOMContentLoaded", function() {
            // Elements
            const notificationBell = document.querySelector('.notification-bell');
            const notificationList = document.getElementById('notifications');
            const notificationCount = document.getElementById('notification-count');

            // Function to fetch and display notifications
            function fetchNotifications() {
                fetch('/api/notifications', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => response.json())
                .then(notifications => {
                    if (notifications.length > 0) {
                        notificationCount.textContent = notifications.length;
                        notificationCount.style.display = 'block';
                    } else {
                        notificationCount.style.display = 'none';
                    }

                    notificationList.innerHTML = ''; // Clear previous notifications
                    notifications.forEach(notification => {
                        let notificationItem = document.createElement('div');
                        notificationItem.textContent = notification.message;
                        notificationItem.classList.add('notification-item');
                        notificationList.appendChild(notificationItem);
                    });
                })
                .catch(error => console.error('Error fetching notifications:', error));
            }

            // Function to mark all notifications as read
            function markAllAsRead() {
                fetch('/api/notifications/mark-all-as-read', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        notificationCount.style.display = 'none';
                    }
                })
                .catch(error => console.error('Error marking notifications as read:', error));
            }

            // Event listener for notification bell click
            notificationBell.addEventListener('click', function() {
                notificationList.classList.toggle('show'); // Toggle visibility of the notification list

                // Mark notifications as read when the list is opened
                if (notificationList.classList.contains('show')) {
                    markAllAsRead();
                }
            });

            // Fetch notifications on page load
            fetchNotifications();
        });
        
        
        
        
        