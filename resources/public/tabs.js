function switchTab(button, tabIndex) {
    const tabContainer = button.closest('.bg-tint-1');
    const buttons = tabContainer.querySelectorAll('button[data-tab]');
    const contents = tabContainer.querySelectorAll('.tab-content');

    buttons.forEach(btn => {
        btn.classList.remove('border-primary-9', 'text-primary-9');
        btn.classList.add('border-transparent', 'text-tint-9');
    });

    contents.forEach(content => {
        content.classList.add('hidden');
        content.classList.remove('block');
    });

    button.classList.remove('border-transparent', 'text-tint-9');
    button.classList.add('border-primary-9', 'text-primary-9');

    const targetContent = tabContainer.querySelector(`.tab-content[data-tab="${tabIndex}"]`);
    if (targetContent) {
        targetContent.classList.remove('hidden');
        targetContent.classList.add('block');
    }
}
