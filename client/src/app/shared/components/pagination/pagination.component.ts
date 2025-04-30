import { Component, Input, Output, EventEmitter, OnChanges } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnChanges {
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Output() pageChange = new EventEmitter<number>();

  pages: number[] = [];

  ngOnChanges(): void {
    this.generatePagination();
  }

  generatePagination(): void {
    this.pages = [];
    // Generate up to 5 page numbers for display
    const startPage = Math.max(0, Math.min(this.currentPage - 2, this.totalPages - 5));
    const endPage = Math.min(this.totalPages - 1, startPage + 4);

    for (let i = startPage; i <= endPage; i++) {
      this.pages.push(i);
    }
  }

  onPageClick(page: number): void {
    this.pageChange.emit(page);
  }
}