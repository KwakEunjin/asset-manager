declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module 'vue' {
  export function ref<T>(value: T): { value: T }
  export function computed<T>(getter: () => T): { value: T }
  export function onMounted(fn: () => void): void
  export function createApp(rootComponent: any): any
  export * from '@vue/runtime-dom'
}