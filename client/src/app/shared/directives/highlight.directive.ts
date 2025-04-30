import { Directive, ElementRef, Input, OnInit, Renderer2 } from '@angular/core';

@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective implements OnInit {
  @Input() appHighlight: number | undefined;

  private static lastHighlightedId = 0;

  constructor(
    private el: ElementRef,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    // If this is a new activity (higher ID than previously seen), highlight it
    if (this.appHighlight && this.appHighlight > HighlightDirective.lastHighlightedId) {
      HighlightDirective.lastHighlightedId = this.appHighlight;
      this.highlight();
    }
  }

  private highlight(): void {
    this.renderer.addClass(this.el.nativeElement, 'new-activity');

    // Remove the class after the animation completes
    setTimeout(() => {
      this.renderer.removeClass(this.el.nativeElement, 'new-activity');
    }, 2000);
  }
}