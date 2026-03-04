document.addEventListener('DOMContentLoaded', function() {
    initSmoothScroll();
    initSolidCards();
    initScrollAnimations();
});

/**
 * Smooth scroll para navegación interna
 */
function initSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Interactividad en cards SOLID
 */
function initSolidCards() {
    const solidItems = document.querySelectorAll('.solid-item');
    
    const descriptions = {
        'S': 'Cada clase/módulo tiene una única responsabilidad. En esta arquitectura: Controller maneja HTTP, Service maneja negocio, Repository maneja datos.',
        'O': 'El código está abierto para extensión pero cerrado para modificación. Nuevas funcionalidades se agregan con nuevas clases que implementan interfaces existentes.',
        'L': 'Las implementaciones pueden sustituir a sus interfaces sin afectar el comportamiento. SecurityService puede reemplazarse por otra implementación de ISecurityService.',
        'I': 'Interfaces específicas por entidad (ISecurityRepository, IBillRepository) en lugar de una interfaz genérica gigante.',
        'D': 'Los módulos de alto nivel (Controller, Service) dependen de abstracciones (IService, IRepository), no de implementaciones concretas.'
    };

    solidItems.forEach(item => {
        item.addEventListener('click', function() {
            const letter = this.querySelector('.letter').textContent;
            showSolidDetail(letter, descriptions[letter]);
        });
    });
}

/**
 * Muestra detalle del principio SOLID
 */
function showSolidDetail(letter, description) {
    let modal = document.getElementById('solid-modal');
    
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'solid-modal';
        modal.innerHTML = `
            <div class="modal-overlay"></div>
            <div class="modal-content">
                <span class="modal-close">&times;</span>
                <div class="modal-letter"></div>
                <div class="modal-text"></div>
            </div>
        `;
        document.body.appendChild(modal);
        
        // Estilos del modal
        const style = document.createElement('style');
        style.textContent = `
            #solid-modal {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                z-index: 1000;
            }
            #solid-modal.active {
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .modal-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
            }
            .modal-content {
                position: relative;
                background: white;
                padding: 40px;
                border-radius: 16px;
                max-width: 500px;
                margin: 20px;
                animation: modalIn 0.3s ease;
            }
            @keyframes modalIn {
                from {
                    opacity: 0;
                    transform: scale(0.9);
                }
                to {
                    opacity: 1;
                    transform: scale(1);
                }
            }
            .modal-close {
                position: absolute;
                top: 16px;
                right: 20px;
                font-size: 28px;
                cursor: pointer;
                color: #64748b;
            }
            .modal-close:hover {
                color: #334155;
            }
            .modal-letter {
                font-size: 4rem;
                font-weight: 800;
                color: #2563eb;
                text-align: center;
                margin-bottom: 16px;
            }
            .modal-text {
                color: #334155;
                line-height: 1.7;
                text-align: center;
            }
        `;
        document.head.appendChild(style);
        
        // Eventos de cierre
        modal.querySelector('.modal-overlay').addEventListener('click', closeModal);
        modal.querySelector('.modal-close').addEventListener('click', closeModal);
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') closeModal();
        });
    }
    
    modal.querySelector('.modal-letter').textContent = letter;
    modal.querySelector('.modal-text').textContent = description;
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeModal() {
    const modal = document.getElementById('solid-modal');
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = '';
    }
}

/**
 * Animaciones al hacer scroll
 */
function initScrollAnimations() {
    const observerOptions = {
        root: null,
        rootMargin: '0px',
        threshold: 0.1
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, observerOptions);

    // Agregar clase para animación
    const style = document.createElement('style');
    style.textContent = `
        .animate-on-scroll {
            opacity: 0;
            transform: translateY(20px);
            transition: opacity 0.6s ease, transform 0.6s ease;
        }
        .animate-on-scroll.visible {
            opacity: 1;
            transform: translateY(0);
        }
    `;
    document.head.appendChild(style);

    // Observar elementos
    document.querySelectorAll('.card, .svg-container, .highlight-box').forEach(el => {
        el.classList.add('animate-on-scroll');
        observer.observe(el);
    });
}
