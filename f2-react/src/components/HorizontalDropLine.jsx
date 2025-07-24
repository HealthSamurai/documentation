import React from 'react';
import { cn } from '../lib/utils';

/**
 * HorizontalDropLine - визуальный индикатор точного места вставки элемента
 * Показывает горизонтальную линию с учетом уровня вложенности
 */
const HorizontalDropLine = ({ 
  isVisible, 
  level = 0, 
  insertionMode = 'after', 
  isValidDrop = true,
  targetTitle = '' 
}) => {
  if (!isVisible) return null;

  // Отступ для показа уровня вложенности (20px на уровень как в DRAG AND DROP RULES)
  const indentLevel = Math.max(0, level);
  const marginLeft = indentLevel * 20;

  // Цвета в зависимости от валидности дропа
  const lineColor = isValidDrop ? 'bg-green-500' : 'bg-red-500';
  const textColor = isValidDrop ? 'text-green-700' : 'text-red-700';
  const bgColor = isValidDrop ? 'bg-green-50' : 'bg-red-50';

  // Текст подсказки в зависимости от режима вставки
  const getInsertionText = () => {
    switch (insertionMode) {
      case 'before':
        return `Insert before "${targetTitle}"`;
      case 'after':
        return `Insert after "${targetTitle}"`;
      case 'into':
        return `Insert into "${targetTitle}"`;
      default:
        return `Insert at level ${level}`;
    }
  };

  return (
    <div 
      className={cn(
        "relative py-1 transition-all duration-200",
        bgColor
      )}
      style={{ 
        marginLeft: `${marginLeft}px`,
        paddingLeft: '8px',
        paddingRight: '8px'
      }}
    >
      {/* Горизонтальная линия вставки */}
      <div className={cn(
        "h-0.5 w-full rounded-full shadow-sm transition-all duration-200",
        lineColor
      )} />
      
      {/* Точка на начале линии для показа уровня */}
      <div 
        className={cn(
          "absolute left-2 top-1/2 -translate-y-1/2 w-2 h-2 rounded-full",
          lineColor
        )}
        style={{ left: `${marginLeft + 4}px` }}
      />
      
      {/* Текстовая подсказка */}
      <div className={cn(
        "absolute left-8 top-1/2 -translate-y-1/2 text-xs font-medium whitespace-nowrap",
        textColor
      )}
      style={{ left: `${marginLeft + 32}px` }}
      >
        {getInsertionText()} • Level {level}
      </div>
    </div>
  );
};

export default HorizontalDropLine;