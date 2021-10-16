import React from 'react'
import { AdminLayout } from '../../layouts/Admin'
import { UserLayout } from '../../layouts/User'

const Dashboard = () => {
  return (
    <h1 className="text-center font-black tracking-tight text-6xl">Hello dashboard page</h1>
  )
}
Dashboard.getLayout = function getLayout(page) {
  return (
    <UserLayout>
      {page}
    </UserLayout>
  )
}

export default Dashboard;
