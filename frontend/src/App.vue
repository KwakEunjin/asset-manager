<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="toggleLeftDrawer"
        />
        
        <q-toolbar-title>
          가계부 & 자산관리
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
    >
      <q-list>
        <q-item-label header>메뉴</q-item-label>
        
        <q-item clickable v-ripple @click="$router.push('/')">
          <q-item-section avatar>
            <q-icon name="dashboard" />
          </q-item-section>
          <q-item-section>대시보드</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="$router.push('/income')">
          <q-item-section avatar>
            <q-icon name="attach_money" />
          </q-item-section>
          <q-item-section>수입관리</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="$router.push('/fixed-expense')">
          <q-item-section avatar>
            <q-icon name="receipt" />
          </q-item-section>
          <q-item-section>고정지출</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="$router.push('/living-expense')">
          <q-item-section avatar>
            <q-icon name="shopping_cart" />
          </q-item-section>
          <q-item-section>생활비</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="$router.push('/savings')">
          <q-item-section avatar>
            <q-icon name="savings" />
          </q-item-section>
          <q-item-section>저축관리</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="$router.push('/short-savings')">
          <q-item-section avatar>
            <q-icon name="account_balance_wallet" />
          </q-item-section>
          <q-item-section>단기저축</q-item-section>
        </q-item>
        
        <q-item clickable v-ripple @click="$router.push('/codes')">
          <q-item-section avatar>
            <q-icon name="settings" />
          </q-item-section>
          <q-item-section>코드관리</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <q-page padding>
        <router-view />
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const leftDrawerOpen = ref(false)

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}
</script>