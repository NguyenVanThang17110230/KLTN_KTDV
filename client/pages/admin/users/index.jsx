import React, { ReactElement, useMemo } from "react";
import { useTable, usePagination } from "react-table";
import { AdminLayout } from "../../../layouts/Admin";
import MOCK_DATA from "../../../public/MOCK_DATA.json";

import { COLUMN_ACOUNT } from "../../../package/account/columns/Columns";

export default function ManagerUsers() {
  const columns = useMemo(() => COLUMN_ACOUNT, []);
  const data = useMemo(() => MOCK_DATA, []);

  const tableInstance = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 },
    },
    usePagination
  );

  const { 
    getTableProps, 
    getTableBodyProps, 
    headerGroups,
    rows,
    prepareRow, 
    page,
    canPreviousPage,
    canNextPage,
    pageOptions,
    pageCount,
    gotoPage,
    nextPage,
    previousPage,
    setPageSize,
    state: { pageIndex, pageSize }
    } = tableInstance;

  return (
    <div className="px-20 py-10">
      <div className="p-3 bg-white rounded-md shadow">
        <div className="mb-4 text-2xl font-semibold">List account</div>
        <table
          className=" w-full table text-gray-600 border-separate space-y-6 text-sm"
          {...getTableProps()}
        >
          <thead className="bg-gray-100 text-gray-500">
            {headerGroups.map((headerGroup, index) => (
              <tr key={index} {...headerGroup.getHeaderGroupProps()}>
                {headerGroup.headers.map((column, index) => (
                  <th className="p-3" key={index} {...column.getHeaderProps()}>
                    {column.render("Header")}
                  </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody {...getTableBodyProps()}>
            {page.map((row, i) => {
              prepareRow(row);
              return (
                <tr className="bg-gray-100" key={i} {...row.getRowProps()}>
                  {row.cells.map((cell, index) => {
                    return (
                      <td className="p-3" key={index} {...cell.getCellProps()}>
                        {cell.render("Cell")}
                      </td>
                    );
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>

        

        <div className="pagination flex items-center justify-center mt-3">
        <button className='p-3 bg-gray-500 disabled:opacity-50 border-2 border-black' onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
          {'First'}
        </button>{' '}
        <button className='p-3 bg-gray-500 disabled:opacity-50' onClick={() => previousPage()} disabled={!canPreviousPage}>
          {'<'}
        </button>{' '}
        <button className='p-3 bg-gray-500 disabled:opacity-50' onClick={() => nextPage()} disabled={!canNextPage}>
          {'>'}
        </button>{' '}
        <button className='p-3 bg-gray-500 disabled:opacity-50' onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
          {'Last'}
        </button>{' '}
        <span>
          Page{' '}
          <strong>
            {pageIndex + 1} of {pageOptions.length}
          </strong>{' '}
        </span>
        <span>
          | Go to page:{' '}
          <input
            type="number"
            className="border-2"
            defaultValue={pageIndex + 1}
            onChange={e => {
              const page = e.target.value ? Number(e.target.value) - 1 : 0
              gotoPage(page)
            }}
            style={{ width: '100px' }}
          />
        </span>{' '}
        <select
          value={pageSize}
          onChange={e => {
            setPageSize(Number(e.target.value))
          }}
        >
          {[10, 20, 30, 40, 50].map(pageSize => (
            <option key={pageSize} value={pageSize}>
              Show {pageSize}
            </option>
          ))}
        </select>
      </div>
      </div>
    </div>
  );
}

ManagerUsers.getLayout = function getLayout(page) {
  return <AdminLayout>{page}</AdminLayout>;
};