import React from 'react'
import type { ReactElement } from 'react'
import { AdminLayout } from '../../layouts/Admin'

export default function Dashboard() {
    return (
        <h1 className="text-center my-24 font-black tracking-tight text-6xl">Hello dashboard page</h1>
    )
}
Dashboard.getLayout = function getLayout(page:ReactElement) {
    return (
      <AdminLayout>
        {page}
      </AdminLayout>
    )
  }
