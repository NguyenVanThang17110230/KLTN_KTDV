import React, { ReactElement } from 'react'

import { AdminLayout } from '../../../layouts/Admin'

export default function ManagerUsers () {

    return (
        <div>
           hello user
        </div>
    )
}

ManagerUsers.getLayout = function getLayout(page) {
    return (
      <AdminLayout>
        {page}
      </AdminLayout>
    )
}