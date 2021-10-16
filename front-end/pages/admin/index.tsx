import React, { ReactElement } from 'react'
import { AdminLayout } from '../../layouts/Admin'
import { CellProps, Column, useTable} from 'react-table'
import { COLUMN_ACOUNT } from '../../package/account/columns/Columns'
import { Account } from '../../package/account/model/Account';


const tableColumns: Array<Column<Account>> = [
    {
      Header: 'Id',
      accessor: 'id',
      width: '40%',
    }
  ];

export default function AdminDashboard () {

    return (
        <div>
           <DataTable
                defaultOrders={[{id: 'status', desc: true}]}
                tableColumns={tableColumns}
                // findData={accountService.findAdmins.bind(accountService)}
                // dataTableRef={(ref): void => {
                //   dataTableRef = ref;
                // }}
              />
        </div>
    )
}

AdminDashboard.getLayout = function getLayout(page:ReactElement) {
    return (
      <AdminLayout>
        {page}
      </AdminLayout>
    )
  }

