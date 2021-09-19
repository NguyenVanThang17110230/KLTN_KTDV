import React, { ReactNode } from "react";
import Sidebar from "../components/Sidebar/Sidebar";
import { NextComponentType } from "next";
import type { ReactElement } from "react";
// interface AdminWrapperServerProps {
//   pageProps?: object;
// }

interface Props {
  children: ReactNode;
}

export const AdminLayout = ( {children}: Props) => {
  return (
    <>
      <Sidebar />
      <div className="relative md:ml-64 bg-blueGray-100">
        {/* <AdminNavbar /> */}
        {/* Header */}
        {/* <HeaderStats /> */}
        <div className="px-4 md:px-10 mx-auto w-full -m-24">
          {children}
          {/* <FooterAdmin /> */}
        </div>
      </div>
    </>
  );
};
