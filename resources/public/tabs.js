function switchTab(button, tabIndex) {
    const tabContainer = button.closest('.bg-white');
    const buttons = tabContainer.querySelectorAll('button[data-tab]');
    const contents = tabContainer.querySelectorAll('.tab-content');

    buttons.forEach(btn => {
        btn.classList.remove('border-blue-500', 'text-blue-600');
        btn.classList.add('border-transparent', 'text-gray-500');
    });

    contents.forEach(content => {
        content.classList.add('hidden');
        content.classList.remove('block');
    });

    button.classList.remove('border-transparent', 'text-gray-500');
    button.classList.add('border-blue-500', 'text-blue-600');

    const targetContent = tabContainer.querySelector(`.tab-content[data-tab="${tabIndex}"]`);
    if (targetContent) {
        targetContent.classList.remove('hidden');
        targetContent.classList.add('block');
    }
}
