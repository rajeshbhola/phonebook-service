document.addEventListener('DOMContentLoaded', () => {
    const contactsTableBody = document.getElementById('contactsTableBody');
    const contactFormContainer = document.getElementById('contactFormContainer');
    const contactForm = document.getElementById('contactForm');
    const formTitle = document.getElementById('formTitle');
    const contactIdInput = document.getElementById('contactId');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const phoneNumbersContainer = document.getElementById('phoneNumbersContainer');
    const addPhoneNumberBtn = document.getElementById('addPhoneNumberBtn');
    const showAddFormBtn = document.getElementById('showAddFormBtn');
    const cancelFormBtn = document.getElementById('cancelFormBtn');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

    let currentContactIdToDelete = null;

    // Function to fetch and display contacts
    async function fetchContacts() {
        const response = await fetch('/api/contacts');
        const contacts = await response.json();
        contactsTableBody.innerHTML = '';
        contacts.forEach(contact => {
            const row = contactsTableBody.insertRow();
            row.innerHTML = `
                <td class="py-4 px-6">${contact.name}</td>
                <td class="py-4 px-6">${contact.phoneNumbers.map(pn => pn.number).join(', ')}</td>
                <td class="py-4 px-6">${contact.email}</td>
                <td class="py-4 px-6">
                    <button class="bg-yellow-500 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded mr-2 edit-btn" data-id="${contact.id}">Edit</button>
                    <button class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded delete-btn" data-id="${contact.id}">Delete</button>
                </td>
            `;
        });

        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', (e) => showEditForm(e.target.dataset.id));
        });

        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', (e) => {
                showDeleteModal(e.target.dataset.id);
            });
        });
    }

    // Function to show add/edit form with animation
    function showForm() {
        contactFormContainer.classList.remove('hidden');
        setTimeout(() => {
            contactFormContainer.classList.remove('opacity-0', 'scale-95');
            contactFormContainer.classList.add('opacity-100', 'scale-100');
        }, 10);
    }

    // Function to hide add/edit form with animation
    function hideForm() {
        contactFormContainer.classList.remove('opacity-100', 'scale-100');
        contactFormContainer.classList.add('opacity-0', 'scale-95');
        setTimeout(() => {
            contactFormContainer.classList.add('hidden');
            contactForm.reset();
            contactIdInput.value = '';
            phoneNumbersContainer.innerHTML = ''; // Clear phone number fields
        }, 300); // Match transition duration
    }

    // Show Add Contact Form
    showAddFormBtn.addEventListener('click', () => {
        formTitle.textContent = 'Add New Contact';
        addPhoneNumberField(); // Add one phone number field by default
        showForm();
    });

    // Cancel Form
    cancelFormBtn.addEventListener('click', hideForm);

    // Add Phone Number Field
    addPhoneNumberBtn.addEventListener('click', () => addPhoneNumberField());

    function addPhoneNumberField(number = '', id = '') {
        const div = document.createElement('div');
        div.classList.add('flex', 'items-center', 'mb-2');
        div.innerHTML = `
            <input type="hidden" class="phoneNumberId" value="${id}">
            <input type="text" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline phoneNumberInput" value="${number}" placeholder="Phone Number" required>
            <button type="button" class="ml-2 bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-2 rounded remove-phone-btn">Remove</button>
        `;
        phoneNumbersContainer.appendChild(div);

        div.querySelector('.remove-phone-btn').addEventListener('click', (e) => e.target.parentNode.remove());
    }

    // Show Edit Form
    async function showEditForm(id) {
        const response = await fetch(`/api/contacts/${id}`);
        const contact = await response.json();

        formTitle.textContent = 'Edit Contact';
        contactIdInput.value = contact.id;
        nameInput.value = contact.name;
        emailInput.value = contact.email;

        phoneNumbersContainer.innerHTML = ''; // Clear existing fields
        if (contact.phoneNumbers && contact.phoneNumbers.length > 0) {
            contact.phoneNumbers.forEach(pn => addPhoneNumberField(pn.number, pn.id));
        } else {
            addPhoneNumberField(); // Add one empty field if no numbers exist
        }
        showForm();
    }

    // Handle Form Submission
    contactForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = contactIdInput.value;
        const name = nameInput.value;
        const email = emailInput.value;
        const phoneNumbers = Array.from(document.querySelectorAll('.phoneNumberInput')).map((input, index) => ({
            id: document.querySelectorAll('.phoneNumberId')[index].value || null,
            number: input.value
        }));

        const contactData = { name, email, phoneNumbers };

        let response;
        if (id) {
            // Update existing contact
            response = await fetch(`/api/contacts/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(contactData),
            });
        } else {
            // Create new contact
            response = await fetch('/api/contacts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(contactData),
            });
        }

        if (response.ok) {
            hideForm();
            fetchContacts();
            showToast(id ? 'Contact updated successfully!' : 'Contact added successfully!');
        } else {
            const errorData = await response.json();
            alert(`Error: ${errorData.message || response.statusText}`);
        }
    });

    // Delete Contact
    confirmDeleteBtn.addEventListener('click', async () => {
        if (currentContactIdToDelete) {
            const response = await fetch(`/api/contacts/${currentContactIdToDelete}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                hideDeleteModal();
                fetchContacts();
                showToast('Contact deleted successfully!');
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message || response.statusText}`);
            }
        }
    });

    // Global functions for modal and toast (from confirm.js, adapted for direct use)
    function showDeleteModal(id) {
        const modal = document.getElementById('deleteModal');
        modal.classList.remove('hidden');
        setTimeout(() => {
            modal.classList.remove('opacity-0');
            modal.classList.add('opacity-100');
        }, 10);
        currentContactIdToDelete = id; // Store the ID for deletion
    }

    function hideDeleteModal() {
        const modal = document.getElementById('deleteModal');
        modal.classList.remove('opacity-100');
        modal.classList.add('opacity-0');
        setTimeout(() => {
            modal.classList.add('hidden');
            currentContactIdToDelete = null;
        }, 300); // Match transition duration
    }

    function showToast(message) {
        const toast = document.getElementById('toast');
        toast.textContent = message;
        toast.classList.remove('hidden');
        setTimeout(() => {
            toast.classList.remove('opacity-0', 'translate-y-full');
            toast.classList.add('opacity-100', 'translate-y-0');
        }, 10);
        setTimeout(() => {
            toast.classList.remove('opacity-100', 'translate-y-0');
            toast.classList.add('opacity-0', 'translate-y-full');
            setTimeout(() => {
                toast.classList.add('hidden');
            }, 300); // Match transition duration
        }, 3000);
    }

    // Initial fetch of contacts
    fetchContacts();
});