import React, { ReactElement } from 'react'
import { AdminLayout } from '../../layouts/Admin'
import { CellProps, Column, useTable} from 'react-table'
import { COLUMN_ACOUNT } from '../../package/account/columns/Columns'
import { Account } from '../../package/account/model/Account';



export default function AdminDashboard () {

    return (
        <div>
           hello
        </div>
    )
}

AdminDashboard.getLayout = function getLayout(page) {
    return (
      <AdminLayout>
        {page}
      </AdminLayout>
    )
}

