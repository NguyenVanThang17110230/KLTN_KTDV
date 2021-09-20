import Head from 'next/head'
import Image from 'next/image'
import type { ReactElement } from 'react'
import { AdminLayout } from '../layouts/Admin'

export default function Home() {
  return (
    <h1 className="text-center my-24 font-black tracking-tight text-6xl">Our homepage</h1>
  )
}

Home.getLayout = function getLayout(page:ReactElement) {
  return (
    <AdminLayout>
      {page}
    </AdminLayout>
  )
}